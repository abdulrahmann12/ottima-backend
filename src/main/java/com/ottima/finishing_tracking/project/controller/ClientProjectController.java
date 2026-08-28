package com.ottima.finishing_tracking.project.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.project.service.ProjectDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/projects")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_PROJECT_CLIENT, description = SwaggerMessages.TAG_PROJECT_CLIENT_DESC)
@PreAuthorize("hasRole('CLIENT')")
public class ClientProjectController {

    private final ProjectDashboardService projectDashboardService;

    @Operation(summary = SwaggerMessages.GET_MY_PROJECTS, description = SwaggerMessages.GET_MY_PROJECTS_DESC)
    @GetMapping
    public ResponseEntity<BaseResponse> getMyProjects(Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECTS_FETCHED, projectDashboardService.getProjectsForClient(pageable)));
    }

    @Operation(summary = SwaggerMessages.GET_PROJECT_DETAILS_CLIENT, description = SwaggerMessages.GET_PROJECT_DETAILS_CLIENT_DESC)
    @GetMapping("/{projectId}")
    public ResponseEntity<BaseResponse> getProjectDetails(@PathVariable UUID projectId) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.PROJECT_FETCHED, projectDashboardService.getProjectDetailsForClient(projectId)));
    }
}