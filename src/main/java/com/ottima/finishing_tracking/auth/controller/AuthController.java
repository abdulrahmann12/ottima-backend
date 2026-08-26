package com.ottima.finishing_tracking.auth.controller;

import com.ottima.finishing_tracking.auth.dto.request.EmailRequestDTO;
import com.ottima.finishing_tracking.auth.dto.request.LoginRequestDTO;
import com.ottima.finishing_tracking.auth.dto.request.RefreshTokenRequest;
import com.ottima.finishing_tracking.auth.dto.request.ResetPasswordRequestDTO;
import com.ottima.finishing_tracking.auth.service.AuthService;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.dto.ChangePasswordRequest;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_AUTH, description = SwaggerMessages.TAG_AUTH_DESC)
public class AuthController {

    private final AuthService authService;


    @Operation(summary = SwaggerMessages.LOGIN, description = SwaggerMessages.LOGIN_DESC)
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(new BaseResponse(Messages.LOGIN_SUCCESS, authService.login(request)));
    }

    @Operation(summary = SwaggerMessages.REFRESH_TOKEN, description = SwaggerMessages.REFRESH_TOKEN_DESC)
    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(new BaseResponse(Messages.TOKEN_REFRESHED, authService.refreshToken(request)));
    }

    @Operation(summary = SwaggerMessages.LOGOUT, description = SwaggerMessages.LOGOUT_DESC)
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(new BaseResponse(Messages.LOGOUT_SUCCESS));
    }

    @Operation(summary = SwaggerMessages.REGENERATE_CODE, description = SwaggerMessages.REGENERATE_CODE_DESC)
    @PostMapping("/regenerate-code")
    public ResponseEntity<BaseResponse> regenerateCode(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
        authService.reGenerateCode(emailRequestDTO);
        return ResponseEntity.ok(new BaseResponse(Messages.VERIFICATION_CODE_REGENERATED));
    }

    @Operation(summary = SwaggerMessages.FORGOT_PASSWORD, description = SwaggerMessages.FORGOT_PASSWORD_DESC)
    @PostMapping("/forget-password")
    public ResponseEntity<BaseResponse> forgetPassword(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
        authService.forgetPassword(emailRequestDTO);
        return ResponseEntity.ok(new BaseResponse(Messages.RESEND_CODE));
    }

    @Operation(summary = SwaggerMessages.RESET_PASSWORD, description = SwaggerMessages.RESET_PASSWORD_DESC)
    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new BaseResponse(Messages.RESET_SUCCESS));
    }

    @Operation(summary = SwaggerMessages.CHANGE_PASSWORD, description = SwaggerMessages.CHANGE_PASSWORD_DESC)
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(new BaseResponse(Messages.PASSWORD_CHANGED));
    }
}
