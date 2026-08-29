package com.ottima.finishing_tracking.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DailyUpdateStatusChangedEvent {
    private UUID dailyUpdateId;
    private Long engineerId;
    private String projectNameAr;
    private String projectNameEn;
    private String newStatus;
}