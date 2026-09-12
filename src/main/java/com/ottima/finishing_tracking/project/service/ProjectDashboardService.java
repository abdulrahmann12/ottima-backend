package com.ottima.finishing_tracking.project.service;

import com.ottima.finishing_tracking.exception.ProjectAccessDeniedException;
import com.ottima.finishing_tracking.exception.ProjectNotFoundException;
import com.ottima.finishing_tracking.project.dto.response.*;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import com.ottima.finishing_tracking.project.mapper.ProjectMapper;
import com.ottima.finishing_tracking.project.repository.AdminProjectSummaryProjection;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectDashboardService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthenticatedUserService authenticatedUserService;

    public Page<ProjectSummaryResponse> getAllProjectsForAdmin(
            String search,
            Long clientId,
            Long engineerId,
            Boolean isDeleted,
            Pageable pageable) {
        String normalizedSearch = normalizeSearch(search);
        return projectRepository.findAdminProjectSummaries(normalizedSearch, clientId, engineerId, isDeleted, pageable)
                .map(this::toAdminProjectSummaryResponse);
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
        return projectRepository.findAllByClient_UserIdAndDeletesAtIsNull(currentClientId, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Cacheable(value = "projectDetails", key = "'client-det-' + #root.target.getCurrentUserId() + '-' + #projectId")
    public ClientProjectResponse getProjectDetailsForClient(UUID projectId) {
        Long currentClientId = authenticatedUserService.getCurrentUser().getUserId();
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (project.getDeletesAt() != null) {
            throw new ProjectNotFoundException();
        }

        if (!project.getClient().getUserId().equals(currentClientId)) {
            throw new ProjectAccessDeniedException();
        }

        return projectMapper.toClientResponse(project);
    }

    @Cacheable(value = "projectsList", key = "'engineer-' + #root.target.getCurrentUserId() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProjectSummaryResponse> getAssignProjectsForEngineer(Pageable pageable) {
        Long currentEngineerId = authenticatedUserService.getCurrentUser().getUserId();
        return projectRepository.findAllByEngineer_UserIdAndDeletesAtIsNull(currentEngineerId, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Cacheable(value = "projectsList", key = "'overview-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProjectSummaryResponse> getAllProjectsOverview(Pageable pageable) {
        return projectRepository.findAllByDeletesAtIsNull(pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Cacheable(value = "projectDetails", key = "'engineer-det-' + #root.target.getCurrentUserId() + '-' + #projectId")
    public EngineerProjectResponse getProjectDetailsForEngineer(UUID projectId) {
        Project project = projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (project.getDeletesAt() != null) {
            throw new ProjectNotFoundException();
        }

        return projectMapper.toEngineerResponse(project);
    }

    public Long getCurrentUserId() {
        return authenticatedUserService.getCurrentUser().getUserId();
    }

    private String normalizeSearch(String search) {
        if (search == null) {
            return null;
        }

        String trimmed = search.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private ProjectSummaryResponse toAdminProjectSummaryResponse(AdminProjectSummaryProjection summary) {
        return ProjectSummaryResponse.builder()
                .projectId(toUuid(summary.getProjectId()))
                .nameAr(summary.getNameAr())
                .nameEn(summary.getNameEn())
                .addressAr(summary.getAddressAr())
                .addressEn(summary.getAddressEn())
                .clientName(summary.getClientName())
                .engineerName(summary.getEngineerName())
                .overallStatus(ProjectStatus.valueOf(summary.getOverallStatus()))
                .targetCompletionDate(summary.getTargetCompletionDate())
                .deletedAt(toInstant(summary.getDeletedAt()))
                .overallProgressPercentage(summary.getOverallProgressPercentage())
                .totalCalculatedSpent(summary.getTotalCalculatedSpent())
                .build();
    }

    private UUID toUuid(Object rawUuid) {
        if (rawUuid == null) {
            return null;
        }

        if (rawUuid instanceof UUID uuid) {
            return uuid;
        }

        if (rawUuid instanceof String uuidString) {
            return UUID.fromString(uuidString);
        }

        if (rawUuid instanceof byte[] uuidBytes) {
            if (uuidBytes.length != 16) {
                throw new IllegalArgumentException("Expected 16-byte UUID value for project summary projection");
            }

            ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
            return new UUID(buffer.getLong(), buffer.getLong());
        }

        throw new IllegalArgumentException(
                "Unsupported project summary UUID type: " + rawUuid.getClass().getName());
    }

    private Instant toInstant(Object rawDateTime) {
        if (rawDateTime == null) {
            return null;
        }

        if (rawDateTime instanceof Instant instant) {
            return instant;
        }

        if (rawDateTime instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toInstant();
        }

        if (rawDateTime instanceof Timestamp timestamp) {
            return timestamp.toInstant();
        }

        if (rawDateTime instanceof LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.UTC);
        }

        if (rawDateTime instanceof String instantString) {
            return Instant.parse(instantString);
        }

        throw new IllegalArgumentException(
                "Unsupported project summary deletedAt type: " + rawDateTime.getClass().getName());
    }
}