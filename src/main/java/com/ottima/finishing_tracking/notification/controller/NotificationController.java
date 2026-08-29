package com.ottima.finishing_tracking.notification.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Manage user in-app notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get my notifications", description = "Fetch a paginated list of notifications for the current user")
    @GetMapping
    public ResponseEntity<BaseResponse> getMyNotifications(Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse("Notifications fetched successfully", notificationService.getMyNotifications(pageable))
        );
    }

    @Operation(summary = "Get unread count", description = "Get the number of unread notifications for the badge icon")
    @GetMapping("/unread-count")
    public ResponseEntity<BaseResponse> getUnreadCount() {
        return ResponseEntity.ok(
                new BaseResponse("Unread count fetched successfully", notificationService.getMyUnreadCount())
        );
    }

    @Operation(summary = "Mark as read", description = "Mark a single notification as read")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<BaseResponse> markAsRead(@PathVariable UUID notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(
                new BaseResponse("Notification marked as read successfully", null)
        );
    }

    @Operation(summary = "Mark all as read", description = "Mark all notifications for the current user as read")
    @PatchMapping("/read-all")
    public ResponseEntity<BaseResponse> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(
                new BaseResponse("All notifications marked as read successfully", null)
        );
    }
}