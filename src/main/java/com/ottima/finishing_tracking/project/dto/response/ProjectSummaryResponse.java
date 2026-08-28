package com.ottima.finishing_tracking.project.dto.response;

import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummaryResponse {
    private UUID projectId;
    private String nameAr;
    private String nameEn;
    private String clientName;
    private String engineerName;
    private ProjectStatus overallStatus;
    private LocalDate targetCompletionDate;

    private BigDecimal overallProgressPercentage;
    private BigDecimal totalCalculatedSpent;
}