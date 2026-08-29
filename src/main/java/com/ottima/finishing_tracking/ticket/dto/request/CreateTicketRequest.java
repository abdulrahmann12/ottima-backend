package com.ottima.finishing_tracking.ticket.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.ticket.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Request body for creating a new internal ticket")
public class CreateTicketRequest {

    @Schema(description = "ID of the user (Admin or Engineer) receiving this ticket", example = "2")
    @NotNull(message = ValidationMessages.TICKET_RECEIVER_REQUIRED)
    private Long receiverId;

    @Schema(description = "Type of the ticket", example = "EXPENSE")
    @NotNull(message = ValidationMessages.TICKET_TYPE_REQUIRED)
    private TicketType ticketType;

    @Schema(description = "Short title for the ticket", example = "طلب عهدة لمشتريات الموقع")
    @NotBlank(message = ValidationMessages.TICKET_TITLE_REQUIRED)
    @Size(max = 255, message = ValidationMessages.TICKET_TITLE_SIZE)
    private String title;

    @Schema(description = "Detailed notes or instructions", example = "برجاء توفير مبلغ 5000 لشراء أسمنت")
    private String description;

    @Schema(description = "Required if the ticket type is EXPENSE", example = "5000.00")
    @PositiveOrZero(message = ValidationMessages.TICKET_AMOUNT_POSITIVE)
    private BigDecimal amount;

    @Valid
    @Schema(description = "List of attached files (invoices, blueprints, etc.)")
    private List<TicketAttachmentRequest> attachments;
}