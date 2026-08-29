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
@Schema(description = "Request body for a client to edit their own comment")
public class EditCommentRequest {

    @Schema(description = "The updated comment text", example = "Updated: I noticed the tiling looks slightly off on the left side.")
    @NotBlank(message = ValidationMessages.COMMENT_TEXT_REQUIRED)
    @Size(max = 1000, message = ValidationMessages.COMMENT_TEXT_SIZE)
    private String clientComment;
}