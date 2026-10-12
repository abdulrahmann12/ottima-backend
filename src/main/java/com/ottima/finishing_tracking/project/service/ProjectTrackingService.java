package com.ottima.finishing_tracking.project.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.exception.ProjectItemNotFoundException;
import com.ottima.finishing_tracking.project.dto.request.UpdateItemProgressRequest;
import com.ottima.finishing_tracking.project.dto.response.ProjectItemResponse;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.project.enums.ProjectItemStatus;
import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import com.ottima.finishing_tracking.project.mapper.ProjectItemMapper;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.exception.ProjectAccessDeniedException;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectTrackingService {

    private final ProjectItemRepository projectItemRepository;
    private final ProjectRepository projectRepository;
    private final ProjectItemMapper projectItemMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.PROJECT_ITEM_ENTITY, details = Messages.PROJECT_ITEM_PROGRESS_UPDATED_LOG)
    @Transactional
    @CacheEvict(value = {"projectsList", "projectDetails", "dashboardSummary"}, allEntries = true)
    public ProjectItemResponse updateItemProgress(UUID projectId, UUID itemId, UpdateItemProgressRequest request) {

        ProjectItem item = projectItemRepository.findByProjectItemIdAndProject_ProjectId(itemId, projectId)
                .orElseThrow(ProjectItemNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser != null && currentUser.getRole() != null && "ENGINEER".equals(currentUser.getRole().getRoleName())
                && !item.getProject().isAssignedToEngineer(currentUser.getUserId())) {
            throw new ProjectAccessDeniedException();
        }

        item.setCompletionPercentage(request.getCompletionPercentage());
        if (request.getCompletionPercentage().compareTo(BigDecimal.ZERO) == 0) {
            item.setStatus(ProjectItemStatus.PENDING);
        } else if (request.getCompletionPercentage().compareTo(new BigDecimal("100.0")) >= 0) {
            item.setStatus(ProjectItemStatus.COMPLETED);
        } else {
            item.setStatus(ProjectItemStatus.IN_PROGRESS);
        }

        ProjectItem updatedItem = projectItemRepository.save(item);

        checkAndUpdateProjectOverallStatus(projectId, item.getProject());

        return projectItemMapper.toResponse(updatedItem);
    }

    private void checkAndUpdateProjectOverallStatus(UUID projectId, Project project) {
        if (project == null) {
            return;
        }

        List<ProjectItem> allItems = projectItemRepository.findAllByProject_ProjectIdOrderBySequenceOrderAsc(projectId);
        if (allItems.isEmpty()) {
            return;
        }

        boolean allItemsCompleted = allItems.stream()
                .allMatch(pi -> pi.getStatus() == ProjectItemStatus.COMPLETED
                        || (pi.getCompletionPercentage() != null && pi.getCompletionPercentage().compareTo(new BigDecimal("100.0")) >= 0));

        BigDecimal totalProgress = BigDecimal.ZERO;
        for (ProjectItem pi : allItems) {
            if (pi.getWeightPercentage() != null && pi.getCompletionPercentage() != null) {
                BigDecimal contribution = pi.getWeightPercentage()
                        .multiply(pi.getCompletionPercentage())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                totalProgress = totalProgress.add(contribution);
            }
        }

        boolean isProgressHundredPercent = totalProgress.compareTo(new BigDecimal("99.99")) >= 0;

        if (allItemsCompleted && isProgressHundredPercent) {
            if (project.getOverallStatus() != ProjectStatus.COMPLETED) {
                project.setOverallStatus(ProjectStatus.COMPLETED);
                projectRepository.save(project);
            }
        } else if (project.getOverallStatus() == ProjectStatus.COMPLETED) {
            project.setOverallStatus(ProjectStatus.ACTIVE);
            projectRepository.save(project);
        }
    }
}