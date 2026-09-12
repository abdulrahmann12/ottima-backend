package com.ottima.finishing_tracking.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContextResponse {
    private UUID projectId;
    private UUID projectItemId;
    private UUID dailyUpdateId;
    private UUID commentId;
    private String projectNameEn;
    private String projectNameAr;
    private String itemNameEn;
    private String itemNameAr;
}
