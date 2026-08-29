package com.ottima.finishing_tracking.logging.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.logging.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get all system logs", description = "Fetch a paginated list of all system activities")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllLogs(Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse("Logs fetched successfully", activityLogService.getAllLogs(pageable))
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