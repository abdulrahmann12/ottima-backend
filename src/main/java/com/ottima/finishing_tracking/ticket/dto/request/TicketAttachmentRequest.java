package com.ottima.finishing_tracking.ticket.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.ticket.enums.AttachmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A single file attachment included with a ticket")
public class TicketAttachmentRequest {

    @Schema(description = "Cloudinary URL of the uploaded file", example = "https://res.cloudinary.com/...")
    @NotBlank(message = ValidationMessages.TICKET_ATTACHMENT_URL_REQUIRED)
    private String fileUrl;

    @Schema(description = "Type of the attachment (IMAGE or PDF)", example = "IMAGE")
    @NotNull(message = ValidationMessages.TICKET_ATTACHMENT_TYPE_REQUIRED)
    private AttachmentType fileType;
}