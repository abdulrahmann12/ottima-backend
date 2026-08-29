package com.ottima.finishing_tracking.financial.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Response object summarizing the financial standing of a project")
public class FinancialSummaryResponse {

    @Schema(description = "Total amount deposited (paid) into the project", example = "50000.00")
    private BigDecimal totalPaidAmount;

    @Schema(description = "Total number of deposit transactions", example = "5")
    private long totalPaidCount;

    @Schema(description = "Total amount spent (expenses) on the project", example = "32000.00")
    private BigDecimal totalSpentAmount;

    @Schema(description = "Total number of expense transactions", example = "8")
    private long totalSpentCount;

    @Schema(description = "Remaining balance (total paid minus total spent)", example = "18000.00")
    private BigDecimal remainingBalance;
}