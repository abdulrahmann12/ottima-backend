package com.ottima.finishing_tracking.comment.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for a client to add a comment on an approved daily update")
public class AddCommentRequest {

    @Schema(description = "The comment text written by the client", example = "Great progress on the wall painting!")
    @NotBlank(message = ValidationMessages.COMMENT_TEXT_REQUIRED)
    @Size(max = 1000, message = ValidationMessages.COMMENT_TEXT_SIZE)
    private String clientComment;
}