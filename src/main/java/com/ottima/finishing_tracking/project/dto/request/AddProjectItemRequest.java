package com.ottima.finishing_tracking.project.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectItemRequest {

    @Schema(description = "ID of the standard item from the catalog")
    @NotNull(message = ValidationMessages.STANDARD_ITEM_ID_REQUIRED)
    private UUID standardItemId;

    @Schema(description = "Specific budget allocated for this item")
    @NotNull(message = ValidationMessages.BUDGET_REQUIRED)
    @Min(value = 0, message = ValidationMessages.BUDGET_MIN)
    private BigDecimal budget;

    @Schema(description = "Weight percentage contribution to the total project (0.01 to 100.00)")
    @NotNull(message = ValidationMessages.WEIGHT_REQUIRED)
    @DecimalMin(value = "0.01", message = ValidationMessages.WEIGHT_MIN)
    @DecimalMax(value = "100.00", message = ValidationMessages.WEIGHT_MAX)
    private BigDecimal weightPercentage;

    @Schema(description = "Execution order (Sequence). Optional, will auto-increment if not provided.")
    private Integer sequenceOrder;

    @Schema(description = "General notes or instructions for the engineer")
    private String generalNotes;
}