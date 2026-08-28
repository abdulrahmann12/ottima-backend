package com.ottima.finishing_tracking.project.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.project.dto.request.*;
import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import com.ottima.finishing_tracking.project.service.ProjectAdminService;
import com.ottima.finishing_tracking.project.service.ProjectDashboardService;
import com.ottima.finishing_tracking.project.service.ProjectTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/projects")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_PROJECT_ADMIN, description = SwaggerMessages.TAG_PROJECT_ADMIN_DESC)
@PreAuthorize("hasRole('ADMIN')")
public class AdminProjectController {

    private final ProjectAdminService projectAdminService;
    private final ProjectDashboardService projectDashboardService;
    private final ProjectTrackingService projectTrackingService;

    // ==========================================
    // Project Management
    // ==========================================

    @Operation(summary = SwaggerMessages.CREATE_PROJECT, description = SwaggerMessages.CREATE_PROJECT_DESC)
    @PostMapping
    public ResponseEntity<BaseResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse(Messages.PROJECT_CREATED, projectAdminService.createProject(request)));
    }

    @Operation(summary = SwaggerMessages.UPDATE_PROJECT, description = SwaggerMessages.UPDATE_PROJECT_DESC)
    @PutMapping("/{projectId}")
    public ResponseEntity<BaseResponse> updateProject(
            @PathVariable UUID projectId,
            @Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_UPDATED, projectAdminService.updateProject(projectId, request)));
    }

    @Operation(summary = SwaggerMessages.DELETE_PROJECT, description = SwaggerMessages.DELETE_PROJECT_DESC)
    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse> deleteProject(@PathVariable UUID projectId) {
        projectAdminService.deleteProject(projectId);
        return ResponseEntity.ok(new BaseResponse(Messages.PROJECT_DELETED));
    }

    @Operation(summary = SwaggerMessages.CHANGE_PROJECT_STATUS, description = SwaggerMessages.CHANGE_PROJECT_STATUS_DESC)
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<BaseResponse> changeProjectStatus(
            @PathVariable UUID projectId,
            @RequestParam ProjectStatus status) {
        projectAdminService.changeProjectStatus(projectId, status);
        return ResponseEntity.ok(new BaseResponse(Messages.PROJECT_STATUS_CHANGED));
    }

    @Operation(summary = SwaggerMessages.GET_ALL_PROJECTS_ADMIN, description = SwaggerMessages.GET_ALL_PROJECTS_ADMIN_DESC)
    @GetMapping
    public ResponseEntity<BaseResponse> getAllProjects(Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECTS_FETCHED, projectDashboardService.getAllProjectsForAdmin(pageable)));
    }

    @Operation(summary = SwaggerMessages.GET_PROJECT_DETAILS_ADMIN, description = SwaggerMessages.GET_PROJECT_DETAILS_ADMIN_DESC)
    @GetMapping("/{projectId}")
    public ResponseEntity<BaseResponse> getProjectDetails(@PathVariable UUID projectId) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_FETCHED, projectDashboardService.getProjectDetailsForAdmin(projectId)));
    }

    // ==========================================
    // Project Items Management
    // ==========================================

    @Operation(summary = SwaggerMessages.ASSIGN_PROJECT_ITEMS, description = SwaggerMessages.ASSIGN_PROJECT_ITEMS_DESC)
    @PostMapping("/{projectId}/items")
    public ResponseEntity<BaseResponse> assignProjectItems(
            @PathVariable UUID projectId,
            @Valid @RequestBody AssignProjectItemsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse(Messages.PROJECT_ITEM_ADDED, projectAdminService.assignProjectItems(projectId, request)));
    }

    @Operation(summary = SwaggerMessages.UPDATE_PROJECT_ITEM_CONFIG, description = SwaggerMessages.UPDATE_PROJECT_ITEM_CONFIG_DESC)
    @PutMapping("/{projectId}/items/{itemId}")
    public ResponseEntity<BaseResponse> updateProjectItemConfig(
            @PathVariable UUID projectId,
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateProjectItemConfigRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_ITEM_UPDATED, projectAdminService.updateProjectItemConfig(projectId, itemId, request)));
    }

    @Operation(summary = SwaggerMessages.REMOVE_PROJECT_ITEM, description = SwaggerMessages.REMOVE_PROJECT_ITEM_DESC)
    @DeleteMapping("/{projectId}/items/{itemId}")
    public ResponseEntity<BaseResponse> removeProjectItem(
            @PathVariable UUID projectId,
            @PathVariable UUID itemId) {
        projectAdminService.removeProjectItem(projectId, itemId);
        return ResponseEntity.ok(new BaseResponse(Messages.PROJECT_ITEM_REMOVED));
    }

    @Operation(summary = SwaggerMessages.UPDATE_ITEM_PROGRESS, description = SwaggerMessages.UPDATE_ITEM_PROGRESS_DESC)
    @PutMapping("/{projectId}/items/{itemId}/progress")
    public ResponseEntity<BaseResponse> updateItemProgress(
            @PathVariable UUID projectId,
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateItemProgressRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_ITEM_PROGRESS_UPDATED,
                        projectTrackingService.updateItemProgress(projectId, itemId, request)));
    }
}