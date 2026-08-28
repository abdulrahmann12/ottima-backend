package com.ottima.finishing_tracking.project.dto.response;

import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private UUID projectId;
    private String nameAr;
    private String nameEn;
    private String addressAr;
    private String addressEn;
    private String clientName;
    private String engineerName;
    private ProjectStatus overallStatus;
    private BigDecimal estimatedBudget;
    private LocalDate startDate;
    private LocalDate targetCompletionDate;

    private BigDecimal overallProgressPercentage; // (sum of: item.weight * item.progress / 100)
    private BigDecimal totalCalculatedSpent;      // (sum of all items calculated spent)

    private List<ProjectItemResponse> items;
}