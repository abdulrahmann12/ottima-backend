package com.ottima.finishing_tracking.project.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.project.enums.ProjectItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemProgressRequest {

    @Schema(description = "Current completion percentage. Can exceed 100% if extra work is done.")
    @NotNull(message = ValidationMessages.PROGRESS_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.PROGRESS_MIN)
    private BigDecimal completionPercentage;

    @Schema(description = "Current status of the item on-site")
    @NotNull(message = "Status is required")
    private ProjectItemStatus status;
}