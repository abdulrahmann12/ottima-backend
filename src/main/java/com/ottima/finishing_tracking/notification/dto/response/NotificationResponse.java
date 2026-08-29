package com.ottima.finishing_tracking.notification.dto.response;

import com.ottima.finishing_tracking.notification.enums.ReferenceType;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NotificationResponse {
    private UUID notificationId;
    private String title;
    private String message;
    private boolean isRead;
    private ReferenceType referenceType;
    private UUID referenceId;
    private Instant createdAt;
}