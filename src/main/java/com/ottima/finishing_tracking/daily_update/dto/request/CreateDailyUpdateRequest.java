package com.ottima.finishing_tracking.daily_update.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request body for creating a daily site update by an assigned engineer")
public class CreateDailyUpdateRequest {

    @Schema(description = "ID of the project item this update belongs to", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    @NotNull(message = ValidationMessages.PROJECT_ITEM_ID_REQUIRED)
    private UUID projectItemId;

    @Schema(description = "Title or summary of the progress update", example = "Completed first layer of wall painting")
    @NotBlank(message = ValidationMessages.TITLE_REQUIRED)
    @Size(max = 255, message = ValidationMessages.TITLE_SIZE)
    private String title;

    @Schema(description = "Detailed notes or remarks from the engineer", example = "All materials passed inspection; next layer scheduled tomorrow.")
    @Size(max = 2000, message = ValidationMessages.NOTES_SIZE)
    private String notes;

    @Schema(description = "List of image URLs captured on site attached to this update", example = "[\"https://res.cloudinary.com/.../img1.jpg\"]")
    private List<String> imageUrls;
}