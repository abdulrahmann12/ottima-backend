package com.ottima.finishing_tracking.ticket.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.ticket.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for updating an existing internal ticket")
public class UpdateTicketRequest {

    @Schema(description = "Updated short title for the ticket", example = "طلب عهدة معدل")
    @Size(max = 255, message = ValidationMessages.TICKET_TITLE_SIZE)
    private String title;

    @Schema(description = "Updated detailed notes or instructions")
    private String description;

    @Schema(description = "Type of the ticket", example = "EXPENSE")
    @NotNull(message = ValidationMessages.TICKET_TYPE_REQUIRED)
    private TicketType ticketType;

    @Schema(description = "Updated amount (for EXPENSE tickets)", example = "7500.00")
    @PositiveOrZero(message = ValidationMessages.TICKET_AMOUNT_POSITIVE)
    private BigDecimal amount;

    @Valid
    @Schema(description = "Updated list of attached files")
    private List<TicketAttachmentRequest> attachments;
}