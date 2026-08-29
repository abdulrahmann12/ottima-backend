package com.ottima.finishing_tracking.comment.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID id;
    private UUID dailyUpdateId;

    private String clientNameAr;
    private String clientNameEn;
    private String clientComment;

    private String adminReply;
    private String repliedByAdminNameAr;
    private String repliedByAdminNameEn;
    private Instant repliedAt;

    private Instant createdAt;
    private Instant updatedAt;
}