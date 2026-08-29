package com.ottima.finishing_tracking.comment.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for admin to reply to a client's comment")
public class ReplyCommentRequest {

    @Schema(description = "The reply text written by the admin", example = "Thank you for the feedback, we will look into this.")
    @NotBlank(message = ValidationMessages.ADMIN_REPLY_REQUIRED)
    @Size(max = 1000, message = ValidationMessages.ADMIN_REPLY_SIZE)
    private String adminReply;
}