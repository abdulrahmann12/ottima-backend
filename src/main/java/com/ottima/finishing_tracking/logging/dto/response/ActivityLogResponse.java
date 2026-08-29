package com.ottima.finishing_tracking.logging.dto.response;

import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
public class ActivityLogResponse {
    private UUID id;
    private Long userId;
    private String username;
    private ActionType action;
    private ActivityStatus status;
    private String entityName;
    private String entityId;
    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;
    private String ipAddress;
    private String userAgent;
    private String details;
    private Instant createdAt;
}