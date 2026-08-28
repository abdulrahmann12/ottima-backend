package com.ottima.finishing_tracking.project.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.project.dto.request.UpdateItemProgressRequest;
import com.ottima.finishing_tracking.project.service.ProjectDashboardService;
import com.ottima.finishing_tracking.project.service.ProjectTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/engineer/projects")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_PROJECT_ENGINEER, description = SwaggerMessages.TAG_PROJECT_ENGINEER_DESC)
@PreAuthorize("hasRole('ENGINEER')")
public class EngineerProjectController {

    private final ProjectDashboardService projectDashboardService;
    private final ProjectTrackingService projectTrackingService;

    @Operation(summary = SwaggerMessages.GET_ALL_PROJECTS_ENGINEER, description = SwaggerMessages.GET_ALL_PROJECTS_ENGINEER_DESC)
    @GetMapping
    public ResponseEntity<BaseResponse> getAllProjects(Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECTS_FETCHED, projectDashboardService.getAllProjectsOverview(pageable)));
    }

    @Operation(summary = SwaggerMessages.GET_ASSIGNED_PROJECTS_ENGINEER, description = SwaggerMessages.GET_ASSIGNED_PROJECTS_ENGINEER_DESC)
    @GetMapping("/assigned")
    public ResponseEntity<BaseResponse> getAssignedProjects(Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECTS_FETCHED, projectDashboardService.getAssignProjectsForEngineer(pageable)));
    }

    @Operation(summary = SwaggerMessages.GET_PROJECT_DETAILS_ENGINEER, description = SwaggerMessages.GET_PROJECT_DETAILS_ENGINEER_DESC)
    @GetMapping("/{projectId}")
    public ResponseEntity<BaseResponse> getProjectDetails(@PathVariable UUID projectId) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_FETCHED, projectDashboardService.getProjectDetailsForEngineer(projectId)));
    }

    @Operation(summary = SwaggerMessages.UPDATE_ITEM_PROGRESS, description = SwaggerMessages.UPDATE_ITEM_PROGRESS_DESC)
    @PutMapping("/{projectId}/items/{itemId}/progress")
    public ResponseEntity<BaseResponse> updateItemProgress(
            @PathVariable UUID projectId,
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateItemProgressRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_ITEM_PROGRESS_UPDATED, projectTrackingService.updateItemProgress(projectId, itemId, request)));
    }
}