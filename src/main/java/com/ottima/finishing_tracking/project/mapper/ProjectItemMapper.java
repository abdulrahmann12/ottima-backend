package com.ottima.finishing_tracking.project.mapper;

import com.ottima.finishing_tracking.project.dto.request.AddProjectItemRequest;
import com.ottima.finishing_tracking.project.dto.request.UpdateProjectItemConfigRequest;
import com.ottima.finishing_tracking.project.dto.response.ClientProjectResponse;
import com.ottima.finishing_tracking.project.dto.response.EngineerProjectResponse;
import com.ottima.finishing_tracking.project.dto.response.ProjectItemResponse;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ProjectItemMapper {

    @Mapping(target = "itemNameAr", source = "standardItem.nameAr")
    @Mapping(target = "itemNameEn", source = "standardItem.nameEn")
    @Mapping(target = "calculatedSpent", ignore = true)
    ProjectItemResponse toResponse(ProjectItem entity);

    @AfterMapping
    default void calculateDynamicFields(ProjectItem entity, @MappingTarget ProjectItemResponse response) {
        if (entity.getBudget() != null && entity.getCompletionPercentage() != null) {
            BigDecimal spent = entity.getBudget()
                    .multiply(entity.getCompletionPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            response.setCalculatedSpent(spent);
        } else {
            response.setCalculatedSpent(BigDecimal.ZERO);
        }
    }

    @Mapping(target = "projectItemId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "standardItem", ignore = true)
    @Mapping(target = "status", ignore = true) // Default PENDING
    @Mapping(target = "completionPercentage", ignore = true) // Default 0
    @Mapping(target = "version", ignore = true)
    ProjectItem toEntity(AddProjectItemRequest request);

    @Mapping(target = "projectItemId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "standardItem", ignore = true)
    void updateItemConfigFromRequest(UpdateProjectItemConfigRequest request, @MappingTarget ProjectItem entity);

    @Mapping(target = "itemNameAr", source = "standardItem.nameAr")
    @Mapping(target = "itemNameEn", source = "standardItem.nameEn")
    @Mapping(target = "calculatedSpent", ignore = true)
    ClientProjectResponse.ClientProjectItemResponse toClientItemResponse(ProjectItem entity);

    @AfterMapping
    default void calculateClientItemSpent(ProjectItem entity, @MappingTarget ClientProjectResponse.ClientProjectItemResponse response) {
        if (entity.getBudget() != null && entity.getCompletionPercentage() != null) {
            BigDecimal spent = entity.getBudget()
                    .multiply(entity.getCompletionPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            response.setCalculatedSpent(spent);
        } else {
            response.setCalculatedSpent(BigDecimal.ZERO);
        }
    }

    @Mapping(target = "itemNameAr", source = "standardItem.nameAr")
    @Mapping(target = "itemNameEn", source = "standardItem.nameEn")
    EngineerProjectResponse.EngineerProjectItemResponse toEngineerItemResponse(ProjectItem entity);
}