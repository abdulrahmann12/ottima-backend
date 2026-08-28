package com.ottima.finishing_tracking.daily_update.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for admin evaluation of a daily update and its attached images")
public class EvaluateDailyUpdateRequest {

    @Schema(description = "The new status of the overall update (APPROVED, REJECTED)", example = "APPROVED")
    @NotNull(message = ValidationMessages.UPDATE_STATUS_REQUIRED)
    private UpdateStatus status;

    @Schema(description = "List of image evaluations to approve or reject specific images")
    private List<@Valid ImageEvaluation> imageEvaluations;

    @Schema(description = "Title or summary of the progress update", example = "Completed first layer of wall painting")
    @NotBlank(message = ValidationMessages.TITLE_REQUIRED)
    @Size(max = 255, message = ValidationMessages.TITLE_SIZE)
    private String title;

    @Schema(description = "Detailed notes or remarks from the engineer", example = "All materials passed inspection; next layer scheduled tomorrow.")
    @Size(max = 2000, message = ValidationMessages.NOTES_SIZE)
    private String notes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Evaluation verdict for a single image")
    public static class ImageEvaluation {

        @Schema(description = "Unique ID of the image attached to the daily update", example = "c7d8e9f0-1a2b-3c4d-5e6f-7a8b9c0d1e2f")
        @NotNull(message = ValidationMessages.UPDATE_IMAGE_ID_REQUIRED)
        private UUID updateImageId;

        @Schema(description = "Approval status for the image (true = approved, false = rejected)", example = "true")
        @NotNull(message = ValidationMessages.APPROVAL_STATUS_REQUIRED)
        private Boolean approved;
    }
}