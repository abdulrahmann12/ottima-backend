package com.ottima.finishing_tracking.common.events;

import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import java.time.Instant;

public record ActivityLogEvent(
        Long userId,
        String username,
        String role,
        ActionType actionType,
        String entityName,
        String details,
        ActivityStatus status,
        String errorMessage,
        String ipAddress,
        String endpoint,
        Instant timestamp
) {}