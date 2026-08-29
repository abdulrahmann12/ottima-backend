package com.ottima.finishing_tracking.user.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.role.entity.Role;
import com.ottima.finishing_tracking.role.repository.RoleRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import com.ottima.finishing_tracking.user.dto.request.UpdateProfileRequest;
import com.ottima.finishing_tracking.user.dto.request.AdminUpdateUserRequest;
import com.ottima.finishing_tracking.user.dto.response.UserResponse;
import com.ottima.finishing_tracking.user.dto.response.UserSummaryResponse;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.mapper.UserMapper;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createBaseUser(@Valid CreateUserRequest request, String roleName) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new EmailAlreadyExistsException();
        }
        if (userRepository.existsByUsername(request.getUsername().trim().toLowerCase())) {
            throw new UsernameAlreadyExistsException();
        }

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(RoleNotFoundException::new);

        User user = User.builder()
                .username(request.getUsername().trim().toLowerCase())
                .email(request.getEmail().trim().toLowerCase())
                .fullNameAr(request.getFullNameAr())
                .fullNameEn(request.getFullNameEn())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.USER_ENTITY, details = Messages.USER_UPDATED_LOG)
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserResponse updateMyProfile(@Valid UpdateProfileRequest request) {
        User user = authenticatedUserService.getCurrentUser();

        if (!user.isActive()) {
            throw new UserNotActiveException();
        }

        validateEmailAndUsernameForUpdate(user, request.getEmail(), request.getUsername());

        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setUsername(request.getUsername().trim().toLowerCase());
        user.setFullNameAr(request.getFullNameAr());
        user.setFullNameEn(request.getFullNameEn());
        user.setPhoneNumber(request.getPhoneNumber());

        return userMapper.toResponse(userRepository.save(user));
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.USER_ENTITY, details = Messages.USER_UPDATED_LOG)
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserResponse updateUserByAdmin(Long userId, @Valid AdminUpdateUserRequest request) {
        User user = userRepository.findByIdWithRole(userId).orElseThrow(UserNotFoundException::new);

        validateEmailAndUsernameForUpdate(user, request.getEmail(), request.getUsername());

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(RoleNotFoundException::new);

        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setUsername(request.getUsername().trim().toLowerCase());
        user.setFullNameAr(request.getFullNameAr());
        user.setFullNameEn(request.getFullNameEn());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(role);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Cacheable(value = "users", key = "#p0")
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findByIdWithRole(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toResponse(user);
    }

    public UserResponse getUserData() {
        User user = authenticatedUserService.getCurrentUser();
        if (!user.isActive()) {
            throw new UserNotActiveException();
        }
        return userMapper.toResponse(user);
    }

    @Cacheable(value = "users", key = "#p0")
    public UserResponse getUserByIdentifier(String identifier) {
        User user = userRepository.findByUsernameOrEmailOrPhoneNumberWithRole(identifier.trim().toLowerCase())
                .orElseThrow(UserNotFoundException::new);
        return userMapper.toResponse(user);
    }

    public Page<UserResponse> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllActive(pageable).map(userMapper::toResponse);
    }

    public Page<UserResponse> findAllDeactivatedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllDeactivated(pageable).map(userMapper::toResponse);
    }

    @LogActivity(actionType = ActionType.DELETE, entityName = Constants.USER_ENTITY, details = Messages.USER_DELETED_LOG)
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.isActive()) {
            throw new UserAlreadyDeactivatedException();
        }
        user.setActive(false);
        user.setDeletesAt(Instant.now());
        userRepository.save(user);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.USER_ENTITY, details = Messages.USER_ACTIVATED_LOG)
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.isActive()) {
            throw new UserAlreadyActivatedException();
        }
        user.setActive(true);
        user.setDeletesAt(null);
        userRepository.save(user);
    }

    public Page<UserSummaryResponse> getUsersByRole(String roleName, int page, int size) {
        Role role = roleRepository.findByRoleName(roleName.trim().toUpperCase()).orElseThrow(RoleNotFoundException::new);
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByRole(role, pageable).map(userMapper::toSummaryResponse);
    }

    public Page<UserSummaryResponse> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.searchUsers(keyword.trim(), pageable).map(userMapper::toSummaryResponse);
    }

    private void validateEmailAndUsernameForUpdate(User user, String newEmail, String newUsername) {
        if (userRepository.existsByEmail(newEmail) && !user.getEmail().equals(newEmail.trim().toLowerCase())) {
            throw new EmailAlreadyExistsException();
        }
        if (userRepository.existsByUsername(newUsername) && !user.getUsername().equals(newUsername.trim().toLowerCase())) {
            throw new UsernameAlreadyExistsException();
        }
    }
}