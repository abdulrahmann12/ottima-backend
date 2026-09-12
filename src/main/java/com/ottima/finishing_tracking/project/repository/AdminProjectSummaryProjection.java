package com.ottima.finishing_tracking.project.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AdminProjectSummaryProjection {
    Object getProjectId();
    String getNameAr();
    String getNameEn();
    String getAddressAr();
    String getAddressEn();
    String getClientName();
    String getEngineerName();
    String getOverallStatus();
    LocalDate getTargetCompletionDate();
    BigDecimal getOverallProgressPercentage();
    BigDecimal getTotalCalculatedSpent();
    Object getDeletedAt();
}