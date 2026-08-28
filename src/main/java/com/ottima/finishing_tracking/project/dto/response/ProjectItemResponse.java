package com.ottima.finishing_tracking.project.dto.response;

import com.ottima.finishing_tracking.project.enums.ProjectItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectItemResponse {
    private UUID projectItemId;
    private String itemNameAr;
    private String itemNameEn;
    private ProjectItemStatus status;
    private BigDecimal budget;
    private BigDecimal weightPercentage;
    private BigDecimal completionPercentage;
    private Integer sequenceOrder;
    private String generalNotes;

    private BigDecimal calculatedSpent; // (budget * completionPercentage / 100)
}