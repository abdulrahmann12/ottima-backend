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
public class CommentAddedEmailEvent {
    private String adminEmail;
    private String adminName;
    private String clientName;
    private String projectNameEn;
    private String projectNameAr;
    private String itemNameEn;
    private String itemNameAr;
    private String commentText;
    private Instant timestamp;
}
