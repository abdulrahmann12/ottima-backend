package com.ottima.finishing_tracking.ticket.dto.response;

import com.ottima.finishing_tracking.ticket.enums.TicketStatus;
import com.ottima.finishing_tracking.ticket.enums.TicketType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class TicketResponse {
    private UUID ticketId;
    private UUID projectId;

    // Sender Details
    private Long senderId;
    private String senderNameAr;
    private String senderNameEn;
    private String senderRole;

    // Receiver Details
    private Long receiverId;
    private String receiverNameAr;
    private String receiverNameEn;
    private String receiverRole;

    private TicketType ticketType;
    private String title;
    private String description;
    private BigDecimal amount;
    private TicketStatus status;

    private List<TicketAttachmentResponse> attachments;

    private Instant createdAt;
    private Instant updatedAt;
}