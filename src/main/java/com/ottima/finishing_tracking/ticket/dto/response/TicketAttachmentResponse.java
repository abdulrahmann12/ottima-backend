package com.ottima.finishing_tracking.ticket.dto.response;

import com.ottima.finishing_tracking.ticket.enums.AttachmentType;
import lombok.Data;

import java.util.UUID;

@Data
public class TicketAttachmentResponse {
    private UUID ticketAttachmentId;
    private String fileUrl;
    private AttachmentType fileType;
}