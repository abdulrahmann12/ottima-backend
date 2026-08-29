package com.ottima.finishing_tracking.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DailyUpdateSubmittedEvent {
    private UUID dailyUpdateId;
    private Long adminId;
    private String engineerName;
    private String projectNameAr;
    private String projectNameEn;
}