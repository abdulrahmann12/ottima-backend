package com.ottima.finishing_tracking.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private long totalActiveUsers;
    private long totalDeactivatedUsers;

    private long totalClients;
    private long totalEngineers;
    private long activeProjects;
    private long completedProjects;
}