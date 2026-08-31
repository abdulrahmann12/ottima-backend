package com.ottima.finishing_tracking.project.service;

import com.ottima.finishing_tracking.exception.ProjectAccessDeniedException;
import com.ottima.finishing_tracking.exception.ProjectNotFoundException;
import com.ottima.finishing_tracking.project.dto.response.*;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.mapper.ProjectMapper;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectDashboardService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Cacheable(value = "projectsList", key = "'admin-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProjectResponse> getAllProjectsForAdmin(Pageable pageable) {

        Page<UUID> projectIdsPage = projectRepository.findAllProjectIds(pageable);

        if (projectIdsPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Project> projects = projectRepository.findAllWithItemsByIds(projectIdsPage.getContent());

        List<ProjectResponse> responseList = projects.stream()
                .map(projectMapper::toResponse)
                .toList();

        return new PageImpl<>(responseList, pageable, projectIdsPage.getTotalElements());
    }

    @Cacheable(value = "projectDetails", key = "#projectId")
    public ProjectResponse getProjectDetailsForAdmin(UUID projectId) {
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return projectMapper.toResponse(project);
    }

    @Cacheable(value = "projectsList", key = "'client-' + #root.target.getCurrentUserId() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProjectSummaryResponse> getProjectsForClient(Pageable pageable) {
        Long currentClientId = authenticatedUserService.getCurrentUser().getUserId();
        return projectRepository.findAllByClient_UserId(currentClientId, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Cacheable(value = "projectDetails", key = "'client-det-' + #root.target.getCurrentUserId() + '-' + #projectId")
    public ClientProjectResponse getProjectDetailsForClient(UUID projectId) {
        Long currentClientId = authenticatedUserService.getCurrentUser().getUserId();
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!project.getClient().getUserId().equals(currentClientId)) {
            throw new ProjectAccessDeniedException();
        }

        return projectMapper.toClientResponse(project);
    }

    @Cacheable(value = "projectsList", key = "'engineer-' + #root.target.getCurrentUserId() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProjectSummaryResponse> getAssignProjectsForEngineer(Pageable pageable) {
        Long currentEngineerId = authenticatedUserService.getCurrentUser().getUserId();
        return projectRepository.findAllByEngineer_UserId(currentEngineerId, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Cacheable(value = "projectsList", key = "'overview-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProjectSummaryResponse> getAllProjectsOverview(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Cacheable(value = "projectDetails", key = "'engineer-det-' + #root.target.getCurrentUserId() + '-' + #projectId")
    public EngineerProjectResponse getProjectDetailsForEngineer(UUID projectId) {
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return projectMapper.toEngineerResponse(project);
    }

    public Long getCurrentUserId() {
        return authenticatedUserService.getCurrentUser().getUserId();
    }
}