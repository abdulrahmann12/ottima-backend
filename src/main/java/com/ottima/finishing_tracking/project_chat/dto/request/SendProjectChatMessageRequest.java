package com.ottima.finishing_tracking.project_chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body to send a message in project global chat")
public class SendProjectChatMessageRequest {

    @Schema(description = "Chat message content (optional if attachments are provided)", example = "يرجى مراجعة توريد السيراميك اليوم")
    private String message;

    @Schema(description = "Optional list of file URLs attached to this message")
    @Builder.Default
    private List<String> attachments = new ArrayList<>();
}
