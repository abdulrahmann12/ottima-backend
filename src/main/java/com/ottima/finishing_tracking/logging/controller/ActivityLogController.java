package com.ottima.finishing_tracking.logging.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import com.ottima.finishing_tracking.logging.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/activity-logs")
@RequiredArgsConstructor
@Tag(name = "System Activity Logs", description = "Monitor user activities and system audits")
@PreAuthorize("hasRole('ADMIN')")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @Operation(summary = "Get system logs", description = "Fetch a paginated list of system activities with optional filters for user, action, status, and entity")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllLogs(
            @Parameter(description = "Filter by User ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "Filter by Action Type") @RequestParam(required = false) ActionType action,
            @Parameter(description = "Filter by Status") @RequestParam(required = false) ActivityStatus status,
            @Parameter(description = "Filter by Entity Name") @RequestParam(required = false) String entityName,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                new BaseResponse("Logs fetched successfully", activityLogService.getLogs(userId, action, status, entityName, pageable))
        );
    }

    @Operation(summary = "Get logs for specific user", description = "Fetch activities for a specific user ID")
    @GetMapping("/users/{userId}")
    public ResponseEntity<BaseResponse> getLogsByUser(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse("User logs fetched successfully", activityLogService.getLogsByUserId(userId, pageable))
        );
    }
}