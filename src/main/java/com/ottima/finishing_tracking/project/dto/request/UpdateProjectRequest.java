package com.ottima.finishing_tracking.project.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    @Schema(description = "ID of the engineer assigned to the project (can be changed)")
    @NotNull(message = ValidationMessages.ENGINEER_ID_REQUIRED)
    private Long engineerId;

    @NotBlank(message = ValidationMessages.PROJECT_NAME_AR_REQUIRED)
    private String nameAr;

    @NotBlank(message = ValidationMessages.PROJECT_NAME_EN_REQUIRED)
    private String nameEn;

    private String addressAr;
    private String addressEn;

    @NotNull(message = "Status is required")
    private ProjectStatus overallStatus;

    @NotNull(message = ValidationMessages.BUDGET_REQUIRED)
    @Min(value = 0, message = ValidationMessages.BUDGET_MIN)
    private BigDecimal estimatedBudget;

    @NotNull(message = ValidationMessages.DATE_REQUIRED)
    private LocalDate startDate;

    @NotNull(message = ValidationMessages.DATE_REQUIRED)
    @Future(message = ValidationMessages.DATE_MUST_BE_IN_FUTURE)
    private LocalDate targetCompletionDate;

}