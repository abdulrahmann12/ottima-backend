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
public class DailyUpdateEvaluatedEmailEvent {
    private String engineerEmail;
    private String engineerName;
    private String adminName;
    private String projectNameEn;
    private String projectNameAr;
    private String itemNameEn;
    private String itemNameAr;
    private String updateTitle;
    private String status;
    private String notes;
    private Instant timestamp;
}
