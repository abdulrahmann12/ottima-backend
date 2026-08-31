package com.ottima.finishing_tracking.project.service;

import com.ottima.finishing_tracking.exception.ProjectAccessDeniedException;
import com.ottima.finishing_tracking.exception.ProjectNotFoundException;
import com.ottima.finishing_tracking.project.dto.response.*;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.mapper.ProjectMapper;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectDashboardService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthenticatedUserService authenticatedUserService;

    public Page<ProjectSummaryResponse> getAllProjectsForAdmin(Pageable pageable) {
        return projectRepository.findAllWithItems(pageable).map(projectMapper::toSummaryResponse);
    }

    public ProjectResponse getProjectDetailsForAdmin(UUID projectId) {
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return projectMapper.toResponse(project);
    }

    public Page<ProjectSummaryResponse> getProjectsForClient(Pageable pageable) {
        Long currentClientId = authenticatedUserService.getCurrentUser().getUserId();
        return projectRepository.findAllByClient_UserId(currentClientId, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    public ClientProjectResponse getProjectDetailsForClient(UUID projectId) {
        Long currentClientId = authenticatedUserService.getCurrentUser().getUserId();
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!project.getClient().getUserId().equals(currentClientId)) {
            throw new ProjectAccessDeniedException();
        }

        return projectMapper.toClientResponse(project);
    }

    public Page<ProjectSummaryResponse> getAssignProjectsForEngineer(Pageable pageable) {
        Long currentEngineerId = authenticatedUserService.getCurrentUser().getUserId();
        return projectRepository.findAllByEngineer_UserId(currentEngineerId, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    public Page<ProjectSummaryResponse> getAllProjectsOverview(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toSummaryResponse);
    }

    public EngineerProjectResponse getProjectDetailsForEngineer(UUID projectId) {
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return projectMapper.toEngineerResponse(project);
    }
}