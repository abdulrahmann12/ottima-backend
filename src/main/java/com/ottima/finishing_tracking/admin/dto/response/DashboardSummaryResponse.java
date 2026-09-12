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
    private long totalAdmins;
    private long totalProjects;
    private long activeProjects;
    private long completedProjects;
    private long pausedProjects;
    private long deliveredProjects;
    private long totalStandardItems;
    private long totalDailyUpdates;
    private long totalComments;
}