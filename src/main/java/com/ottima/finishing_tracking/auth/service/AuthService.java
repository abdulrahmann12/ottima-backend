package com.ottima.finishing_tracking.auth.service;

import com.ottima.finishing_tracking.auth.dto.request.EmailRequestDTO;
import com.ottima.finishing_tracking.auth.dto.request.LoginRequestDTO;
import com.ottima.finishing_tracking.auth.dto.request.RefreshTokenRequest;
import com.ottima.finishing_tracking.auth.dto.request.ResetPasswordRequestDTO;
import com.ottima.finishing_tracking.auth.dto.response.AuthResponse;
import com.ottima.finishing_tracking.common.dto.ChangePasswordRequest;
import com.ottima.finishing_tracking.common.events.CodeRegeneratedEvent;
import com.ottima.finishing_tracking.common.events.PasswordResetRequestedEvent;
import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.jwt.RefreshTokenProperties;
import com.ottima.finishing_tracking.jwt.entity.Token;
import com.ottima.finishing_tracking.jwt.repository.TokenRepository;
import com.ottima.finishing_tracking.jwt.service.JwtService;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import static com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants.*;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final RefreshTokenProperties refreshTokenProperties;
    private final RabbitTemplate rabbitTemplate;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public AuthResponse login(@Valid LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsernameOrEmailOrPhoneNumberWithRole(loginRequestDTO.getUsernameOrEmailOrNumber())
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActive()) {
            throw new UserNotActiveException();
        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash())) {
            throw new WrongPasswordException();
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken();
        String hashedRefresh = hashToken(refreshToken);

        Instant expiresAt = Instant.now().plus(refreshTokenProperties.getExpirationMinutes());

        tokenRepository.revokeAllRefreshTokensByUser(user.getUserId());

        tokenRepository.save(Token.builder()
                .user(user)
                .token(hashedRefresh)
                .expired(false)
                .revoked(false)
                .expiresAt(expiresAt)
                .build());
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public void reGenerateCode(@Valid EmailRequestDTO emailRequestDTO){
        User user = userRepository.findForUpdateByIdentifier(emailRequestDTO.getIdentifier())
                .orElseThrow(UserNotFoundException::new);

        if (user.getRequestCode() != null &&
                Instant.now().isBefore(user.getRequestCodeExpiresAt())) {

            throw new VerificationCodeAlreadySentException();
        }
        String newCode = generateConfirmationCode();
        user.setRequestCode(newCode);
        user.setRequestCodeExpiresAt(Instant.now().plusSeconds(5 * 60)); // Code valid for 5 minutes
        CodeRegeneratedEvent codeRegeneratedEvent = new CodeRegeneratedEvent(
                user.getEmail(),
                user.getUsername(),
                newCode,
                Instant.now()
        );
        rabbitTemplate.convertAndSend(AUTH_EXCHANGE, CODE_REGENERATED_KEY, codeRegeneratedEvent);
    }

    @Transactional
    public void forgetPassword(@Valid EmailRequestDTO emailRequestDTO){
        User user = userRepository.findForUpdateByIdentifier(emailRequestDTO.getIdentifier())
                .orElseThrow(UserNotFoundException::new);
        if(!user.isActive()){
            throw new UserNotActiveException();
        }

        if (user.getRequestCode() != null &&
                Instant.now().isBefore(user.getRequestCodeExpiresAt())) {
            throw new VerificationCodeAlreadySentException();
        }
        String newCode = generateConfirmationCode();
        user.setRequestCode(newCode);
        user.setRequestCodeExpiresAt(Instant.now().plusSeconds(5 * 60)); // Code valid for 5 minutes
        PasswordResetRequestedEvent passwordResetRequestedEvent = new PasswordResetRequestedEvent(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                newCode,
                Instant.now()
        );
        rabbitTemplate.convertAndSend(AUTH_EXCHANGE, PASSWORD_RESET_KEY, passwordResetRequestedEvent);
    }


    @Transactional
    public void changePassword(@Valid ChangePasswordRequest changePasswordRequestDTO){

        User user = authenticatedUserService.getCurrentUser();

        if (!passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPasswordHash())) {
            throw new WrongPasswordException();
        }
        user.setPasswordHash(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
    }

    @Transactional
    public void resetPassword(@Valid ResetPasswordRequestDTO resetPasswordRequestDTO){
        User user = userRepository.findByUsernameOrEmailOrPhoneNumberWithRole(resetPasswordRequestDTO.getUsernameOrEmailOrPhoneNumber())
                .orElseThrow(UserNotFoundException::new);
        String requestCode = user.getRequestCode();

        if(user.getRequestCodeExpiresAt() == null || Instant.now().isAfter(user.getRequestCodeExpiresAt())) {
            user.setRequestCode(null);
            user.setRequestCodeExpiresAt(null);
            userRepository.save(user);
            throw new VerificationCodeExpiredException();
        }

        if (requestCode == null || !requestCode.equals(resetPasswordRequestDTO.getCode())) {
            throw new InvalidVerificationCodeException();
        }
        user.setPasswordHash(passwordEncoder.encode(resetPasswordRequestDTO.getNewPassword()));
        user.setRequestCode(null);
        user.setRequestCodeExpiresAt(null);
    }
    public String generateConfirmationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    @Transactional
    public AuthResponse refreshToken(@Valid RefreshTokenRequest request) {
        String hashed = hashToken(request.getRefreshToken());
        Token token = tokenRepository.findByTokenWithUser(hashed)
                .orElseThrow(InvalidTokenException::new);

        if (token.isExpired() || token.isRevoked()) {
            throw new InvalidTokenException();
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            token.setExpired(true);
            tokenRepository.save(token);
            throw new InvalidTokenException();
        }

        User user = token.getUser();
        if (!user.isActive()) {
            throw new UserNotActiveException();
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = generateRefreshToken();
        String hashedNew = hashToken(newRefreshToken);
        Instant expiresAt = Instant.now().plus(refreshTokenProperties.getExpirationMinutes());

        // Rotate refresh token — revoke old, save new
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);

        tokenRepository.save(Token.builder()
                .user(user)
                .token(hashedNew)
                .expired(false)
                .revoked(false)
                .expiresAt(expiresAt)
                .build());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        String hashed = hashToken(request.getRefreshToken());
        Token token = tokenRepository.findByTokenWithUser(hashed)
                .orElseThrow(InvalidTokenException::new);
        if (token.isRevoked() || token.isExpired()) {
            throw new AlreadyLoggedOutException();
        }
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }
}
