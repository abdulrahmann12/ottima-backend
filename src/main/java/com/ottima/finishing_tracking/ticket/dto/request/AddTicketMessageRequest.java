package com.ottima.finishing_tracking.ticket.dto.request;

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
@Schema(description = "Request body to add a new message into a ticket chat thread")
public class AddTicketMessageRequest {

    @Schema(description = "Message content (optional if files attached)", example = "تم الاطلاع على الطلب وجاري توفير المبلغ")
    private String message;

    @Schema(description = "Optional list of file URLs attached to this message")
    @Builder.Default
    private List<String> fileUrls = new ArrayList<>();
}
