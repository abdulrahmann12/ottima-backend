package com.ottima.finishing_tracking.user.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.user.dto.request.AdminUpdateUserRequest;
import com.ottima.finishing_tracking.user.dto.request.UpdateProfileRequest;
import com.ottima.finishing_tracking.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_USER, description = SwaggerMessages.TAG_USER_DESC)
public class UserController {

    private final UserService userService;

    @Operation(summary = SwaggerMessages.UPDATE_USER, description = SwaggerMessages.UPDATE_USER_DESC)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/data")
    public ResponseEntity<BaseResponse> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(new BaseResponse(Messages.USER_UPDATED, userService.updateMyProfile(request)));
    }

    @Operation(summary = SwaggerMessages.UPDATE_USER_BY_ADMIN, description = SwaggerMessages.UPDATE_USER_BY_ADMIN_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse> updateUserByAdmin(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUpdateUserRequest request) {
        return ResponseEntity.ok(new BaseResponse(Messages.USER_UPDATED, userService.updateUserByAdmin(userId, request)));
    }

    @Operation(summary = SwaggerMessages.GET_USER_BY_ID, description = SwaggerMessages.GET_USER_BY_ID_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(new BaseResponse(Messages.USER_FETCHED, userService.getUserById(userId)));
    }

    @Operation(summary = SwaggerMessages.GET_USER_DATA, description = SwaggerMessages.GET_USER_DATA_DESC)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/data")
    public ResponseEntity<BaseResponse> getUserData() {
        return ResponseEntity.ok(new BaseResponse(Messages.USER_FETCHED, userService.getUserData()));
    }

    @Operation(summary = SwaggerMessages.GET_USER_BY_IDENTIFIER, description = SwaggerMessages.GET_USER_BY_IDENTIFIER_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/find/{identifier}")
    public ResponseEntity<BaseResponse> getUserByIdentifier(@PathVariable String identifier) {
        return ResponseEntity.ok(new BaseResponse(Messages.USER_FETCHED, userService.getUserByIdentifier(identifier)));
    }

    @Operation(summary = SwaggerMessages.GET_ALL_USERS, description = SwaggerMessages.GET_ALL_USERS_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse(Messages.USERS_FETCHED, userService.findAllUsers(page, size)));
    }

    @Operation(summary = SwaggerMessages.GET_DEACTIVATED_USERS, description = SwaggerMessages.GET_DEACTIVATED_USERS_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deactivated")
    public ResponseEntity<BaseResponse> getAllDeactivatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse(Messages.USERS_FETCHED, userService.findAllDeactivatedUsers(page, size)));
    }

    @Operation(summary = SwaggerMessages.DELETE_USER, description = SwaggerMessages.DELETE_USER_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new BaseResponse(Messages.USER_DELETED));
    }

    @Operation(summary = SwaggerMessages.ACTIVATE_USER, description = SwaggerMessages.ACTIVATE_USER_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<BaseResponse> activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
        return ResponseEntity.ok(new BaseResponse(Messages.USER_ACTIVATED));
    }

    @Operation(summary = SwaggerMessages.SEARCH_USERS, description = SwaggerMessages.SEARCH_USERS_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse(Messages.USERS_FETCHED, userService.searchUsers(keyword, page, size)));
    }

    @Operation(summary = SwaggerMessages.GET_USERS_BY_ROLE, description = SwaggerMessages.GET_USERS_BY_ROLE_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{roleName}")
    public ResponseEntity<BaseResponse> getUsersByRole(
            @PathVariable String roleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse(Messages.USERS_FETCHED, userService.getUsersByRole(roleName, page, size)));
    }
}