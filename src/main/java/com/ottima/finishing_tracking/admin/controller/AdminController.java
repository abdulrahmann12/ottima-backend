package com.ottima.finishing_tracking.admin.controller;

import com.ottima.finishing_tracking.admin.service.AdminService;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_ADMIN, description = SwaggerMessages.TAG_ADMIN_DESC)
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = SwaggerMessages.CREATE_ADMIN_USER, description = SwaggerMessages.CREATE_ADMIN_USER_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse> createAdmin(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.ADMIN_CREATED, adminService.createAdmin(request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_ALL_ADMINS, description = SwaggerMessages.GET_ALL_ADMINS_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.ADMIN_FETCHED, adminService.getAllAdmins(page, size))
        );
    }

    @Operation(summary = SwaggerMessages.GET_DASHBOARD_SUMMARY, description = SwaggerMessages.GET_DASHBOARD_SUMMARY_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<BaseResponse> getDashboardSummary() {
        return ResponseEntity.ok(
                new BaseResponse(Messages.DASHBOARD_SUMMARY_FETCHED, adminService.getDashboardSummary())
        );
    }
}