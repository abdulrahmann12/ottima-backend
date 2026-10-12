package com.ottima.finishing_tracking.project_chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for admin to forward a client comment to project chat")
public class ForwardClientCommentRequest {

    @NotNull(message = "Comment ID is required")
    @Schema(description = "ID of the client comment to forward", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID commentId;

    @Schema(description = "Optional additional instructions from admin to the engineers", example = "برجاء حل هذه الملاحظة فوراً والتأكد من دهان الوجه الثاني")
    private String additionalNote;
}
