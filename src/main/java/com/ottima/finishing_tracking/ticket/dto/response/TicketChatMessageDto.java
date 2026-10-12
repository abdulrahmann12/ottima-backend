package com.ottima.finishing_tracking.ticket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Message object stored within ticket chat thread (JSONB)")
public class TicketChatMessageDto implements Serializable {

    private UUID messageId;
    private Long senderId;
    private String senderName;
    private String senderRole;
    private String message;

    @Builder.Default
    private List<String> fileUrls = new ArrayList<>();

    private Instant sentAt;
}
