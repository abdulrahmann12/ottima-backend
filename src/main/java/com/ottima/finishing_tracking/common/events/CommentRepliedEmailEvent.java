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
public class CommentRepliedEmailEvent {
    private String clientEmail;
    private String clientName;
    private String adminName;
    private String projectNameEn;
    private String projectNameAr;
    private String clientComment;
    private String adminReply;
    private Instant timestamp;
}
