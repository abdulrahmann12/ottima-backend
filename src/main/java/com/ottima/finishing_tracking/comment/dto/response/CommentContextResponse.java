package com.ottima.finishing_tracking.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentContextResponse {
    private UUID commentId;
    private UUID dailyUpdateId;
    private UUID projectItemId;
    private UUID projectId;
    private String projectNameAr;
    private String projectNameEn;
    private String itemNameAr;
    private String itemNameEn;
}
