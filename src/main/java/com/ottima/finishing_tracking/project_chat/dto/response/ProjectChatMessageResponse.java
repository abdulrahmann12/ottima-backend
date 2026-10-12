package com.ottima.finishing_tracking.project_chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for a message in project global chat")
public class ProjectChatMessageResponse {

    private UUID messageId;
    private UUID projectId;

    private Long senderId;
    private String senderName;
    private String senderRole;

    private String message;
    private List<String> attachments;

    private boolean forwardedFromClient;
    private UUID dailyUpdateId;
    private String dailyUpdateTitle;
    private UUID clientCommentId;
    private String clientName;

    private Instant createdAt;
}
