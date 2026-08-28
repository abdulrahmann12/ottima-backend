package com.ottima.finishing_tracking.project.dto.response;

import com.ottima.finishing_tracking.project.enums.ProjectItemStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ClientProjectResponse {
    private UUID projectId;
    private String nameAr;
    private String nameEn;
    private BigDecimal overallProgressPercentage;
    private BigDecimal totalCalculatedSpent;

    private List<ClientProjectItemResponse> items;

    @Data
    public static class ClientProjectItemResponse {
        private UUID projectItemId;
        private String itemNameAr;
        private String itemNameEn;
        private ProjectItemStatus status;
        private BigDecimal weightPercentage;
        private BigDecimal completionPercentage;
        private BigDecimal calculatedSpent;
    }
}