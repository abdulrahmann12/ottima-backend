package com.ottima.finishing_tracking.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreatedEmailEvent {
    private String receiverEmail;
    private String receiverName;
    private String senderName;
    private String senderRole;
    private String projectNameEn;
    private String projectNameAr;
    private String ticketType;
    private String ticketTitle;
    private String description;
    private BigDecimal amount;
    private UUID ticketId;
    private UUID projectId;
    private Instant timestamp;
}
