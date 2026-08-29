package com.ottima.finishing_tracking.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TicketCreatedEvent {
    private UUID ticketId;
    private Long receiverId;
    private String senderRole;
    private String senderNameAR;
    private String senderNameEn;
    private String ticketTitle;
}
