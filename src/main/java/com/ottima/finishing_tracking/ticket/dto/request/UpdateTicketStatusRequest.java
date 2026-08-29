package com.ottima.finishing_tracking.ticket.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.ticket.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for changing the status of an internal ticket")
public class UpdateTicketStatusRequest {

    @Schema(description = "New status of the ticket", example = "APPROVED")
    @NotNull(message = ValidationMessages.TICKET_STATUS_REQUIRED)
    private TicketStatus status;
}