package com.ottima.finishing_tracking.project.mapper;

import com.ottima.finishing_tracking.project.dto.request.CreateProjectRequest;
import com.ottima.finishing_tracking.project.dto.request.UpdateProjectRequest;
import com.ottima.finishing_tracking.project.dto.response.ClientProjectResponse;
import com.ottima.finishing_tracking.project.dto.response.EngineerProjectResponse;
import com.ottima.finishing_tracking.project.dto.response.ProjectResponse;
import com.ottima.finishing_tracking.project.dto.response.ProjectSummaryResponse;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

@Mapper(componentModel = "spring", uses = {ProjectItemMapper.class}, builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ProjectMapper {

    @Mapping(target = "projectId", source = "projectId")
    @Mapping(target = "clientName", source = "client.username")
    @Mapping(target = "engineerName", source = "engineer.username")
    @Mapping(target = "items", source = "projectItems")
    @Mapping(target = "overallProgressPercentage", ignore = true)
    @Mapping(target = "totalCalculatedSpent", ignore = true)
    ProjectResponse toResponse(Project entity);

    @Mapping(target = "projectId", source = "projectId")
    @Mapping(target = "clientName", source = "client.username")
    @Mapping(target = "engineerName", source = "engineer.username")
    @Mapping(target = "overallProgressPercentage", ignore = true)
    @Mapping(target = "totalCalculatedSpent", ignore = true)
    ProjectSummaryResponse toSummaryResponse(Project entity);

    @AfterMapping
    default void calculateProjectTotalsForResponse(Project entity, @MappingTarget ProjectResponse response) {
        calculateTotals(entity, response::setOverallProgressPercentage, response::setTotalCalculatedSpent);
    }

    @AfterMapping
    default void calculateProjectTotalsForSummary(Project entity, @MappingTarget ProjectSummaryResponse response) {
        calculateTotals(entity, response::setOverallProgressPercentage, response::setTotalCalculatedSpent);
    }

    default void calculateTotals(Project entity, Consumer<BigDecimal> setProgress, Consumer<BigDecimal> setSpent) {
        BigDecimal totalProgress = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;

        if (entity.getProjectItems() != null && !entity.getProjectItems().isEmpty()) {
            for (ProjectItem item : entity.getProjectItems()) {
                if (item.getWeightPercentage() != null && item.getCompletionPercentage() != null) {
                    BigDecimal progressContribution = item.getWeightPercentage()
                            .multiply(item.getCompletionPercentage())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    totalProgress = totalProgress.add(progressContribution);
                }

                if (item.getBudget() != null && item.getCompletionPercentage() != null) {
                    BigDecimal itemSpent = item.getBudget()
                            .multiply(item.getCompletionPercentage())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    totalSpent = totalSpent.add(itemSpent);
                }
            }
        }
        setProgress.accept(totalProgress);
        setSpent.accept(totalSpent);
    }

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "engineer", ignore = true)
    @Mapping(target = "overallStatus", constant = "PENDING")
    @Mapping(target = "projectItems", ignore = true)
    Project toEntity(CreateProjectRequest request);

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "engineer", ignore = true)
    void updateProjectFromRequest(UpdateProjectRequest request, @MappingTarget Project entity);

    @Mapping(target = "projectId", source = "projectId")
    @Mapping(target = "items", source = "projectItems")
    @Mapping(target = "overallProgressPercentage", ignore = true)
    @Mapping(target = "totalCalculatedSpent", ignore = true)
    ClientProjectResponse toClientResponse(Project entity);

    @AfterMapping
    default void calculateClientTotalsForResponse(Project entity, @MappingTarget ClientProjectResponse response) {
        calculateTotals(entity, response::setOverallProgressPercentage, response::setTotalCalculatedSpent);
    }

    @Mapping(target = "projectId", source = "projectId")
    @Mapping(target = "clientName", source = "client.username")
    @Mapping(target = "engineerName", source = "engineer.username")
    @Mapping(target = "items", source = "projectItems")
    @Mapping(target = "overallProgressPercentage", ignore = true)
    EngineerProjectResponse toEngineerResponse(Project entity);

    @AfterMapping
    default void calculateEngineerTotalsForResponse(Project entity, @MappingTarget EngineerProjectResponse response) {
        calculateTotals(entity, response::setOverallProgressPercentage, dummySpent -> {});
    }
}