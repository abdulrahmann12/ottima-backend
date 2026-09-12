package com.ottima.finishing_tracking.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyUpdateCreatedEmailEvent {
    private String adminEmail;
    private String adminName;
    private String engineerName;
    private String projectNameEn;
    private String projectNameAr;
    private String itemNameEn;
    private String itemNameAr;
    private String updateTitle;
    private String notes;
    private Instant timestamp;
}
