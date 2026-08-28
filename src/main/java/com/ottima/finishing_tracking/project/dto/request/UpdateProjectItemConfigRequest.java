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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectItemConfigRequest {

    @NotNull(message = ValidationMessages.BUDGET_REQUIRED)
    @Min(value = 0, message = ValidationMessages.BUDGET_MIN)
    private BigDecimal budget;

    @NotNull(message = ValidationMessages.WEIGHT_REQUIRED)
    @DecimalMin(value = "0.01", message = ValidationMessages.WEIGHT_MIN)
    @DecimalMax(value = "100.00", message = ValidationMessages.WEIGHT_MAX)
    private BigDecimal weightPercentage;

    @Schema(description = "Execution order (Sequence).")
    @NotNull(message = "Sequence order is required")
    private Integer sequenceOrder;

    private String generalNotes;
}