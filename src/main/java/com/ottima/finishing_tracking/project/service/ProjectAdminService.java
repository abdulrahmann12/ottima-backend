package com.ottima.finishing_tracking.project.service;

import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.project.dto.request.AddProjectItemRequest;
import com.ottima.finishing_tracking.project.dto.request.CreateProjectRequest;
import com.ottima.finishing_tracking.project.dto.request.UpdateProjectItemConfigRequest;
import com.ottima.finishing_tracking.project.dto.request.UpdateProjectRequest;
import com.ottima.finishing_tracking.project.dto.request.AssignProjectItemsRequest;
import com.ottima.finishing_tracking.project.dto.response.ProjectItemResponse;
import com.ottima.finishing_tracking.project.dto.response.ProjectResponse;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import com.ottima.finishing_tracking.project.mapper.ProjectItemMapper;
import com.ottima.finishing_tracking.project.mapper.ProjectMapper;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.standard_item.entity.StandardItem;
import com.ottima.finishing_tracking.standard_item.repository.StandardItemRepository;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import com.ottima.finishing_tracking.common.messages.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectAdminService {

    private final ProjectRepository projectRepository;
    private final ProjectItemRepository projectItemRepository;
    private final UserRepository userRepository;
    private final StandardItemRepository standardItemRepository;
    private final ProjectMapper projectMapper;
    private final ProjectItemMapper projectItemMapper;

    // ==========================================
    // Project Management
    // ==========================================

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        User client = validateAndGetUserByRole(request.getClientId(), "CLIENT", Messages.USER_NOT_CLIENT,Messages.CLIENT_NOT_FOUND);
        User engineer = validateAndGetUserByRole(request.getEngineerId(), "ENGINEER", Messages.USER_NOT_ENGINEER,Messages.ENGINEER_NOT_FOUND);

        Project project = projectMapper.toEntity(request);
        project.setClient(client);
        project.setEngineer(engineer);

        Project savedProject = projectRepository.save(project);
        return projectMapper.toResponse(savedProject);
    }

    @Transactional
    public ProjectResponse updateProject(UUID projectId, UpdateProjectRequest request) {
        Project project = getProjectById(projectId);

        if (!project.getEngineer().getUserId().equals(request.getEngineerId())) {
            User newEngineer = validateAndGetUserByRole(request.getEngineerId(), "ENGINEER", Messages.USER_NOT_ENGINEER,Messages.ENGINEER_NOT_FOUND);
            project.setEngineer(newEngineer);
        }

        projectMapper.updateProjectFromRequest(request, project);
        Project updatedProject = projectRepository.save(project);

        return projectMapper.toResponse(updatedProject);
    }

    @Transactional
    public List<ProjectItemResponse> assignProjectItems(UUID projectId, AssignProjectItemsRequest request) {
        Project project = getProjectById(projectId);

        BigDecimal incomingTotalWeight = request.getItems().stream()
                .map(AddProjectItemRequest::getWeightPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        validateWeightLimit(projectId, incomingTotalWeight, null);

        Integer dbMaxSeq = projectItemRepository.findMaxSequenceOrderByProjectId(projectId);
        int currentMaxSequence = (dbMaxSeq != null) ? dbMaxSeq : 0;

        List<ProjectItem> itemsToSave = new ArrayList<>();

        for (AddProjectItemRequest itemRequest : request.getItems()) {
            StandardItem standardItem = standardItemRepository.findById(itemRequest.getStandardItemId())
                    .orElseThrow(StandardItemNotFoundException::new);

            if (projectItemRepository.existsByProject_ProjectIdAndStandardItem_ItemId(projectId, standardItem.getItemId())) {
                throw new ProjectItemAlreadyExistsException();
            }

            ProjectItem projectItem = projectItemMapper.toEntity(itemRequest);
            projectItem.setProject(project);
            projectItem.setStandardItem(standardItem);

            if (itemRequest.getSequenceOrder() != null) {
                projectItem.setSequenceOrder(itemRequest.getSequenceOrder());
            } else if (standardItem.getDefaultSequence() != null) {
                projectItem.setSequenceOrder(standardItem.getDefaultSequence());
            } else {
                currentMaxSequence++;
                projectItem.setSequenceOrder(currentMaxSequence);
            }
            itemsToSave.add(projectItem);
        }

        return projectItemRepository.saveAll(itemsToSave).stream()
                .map(projectItemMapper::toResponse)
                .toList();
    }
    private User validateAndGetUserByRole(Long userId, String expectedRole, String errorMessage, String userTypeNotFoundMessage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userTypeNotFoundMessage));

        String roleName = user.getRole().getRoleName();
        if (!roleName.equals(expectedRole) && !roleName.equals("ROLE_" + expectedRole)) {
            throw new InvalidRoleException(errorMessage);
        }
        return user;
    }

    private Project getProjectById(UUID projectId) {
        return projectRepository.findByIdWithItems(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    private void validateWeightLimit(UUID projectId, BigDecimal newWeight, UUID excludedItemId) {
        BigDecimal currentTotalWeight = projectItemRepository.sumWeightPercentageByProjectId(projectId);

        if (excludedItemId != null) {
            ProjectItem existingItem = projectItemRepository.findById(excludedItemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            currentTotalWeight = currentTotalWeight.subtract(existingItem.getWeightPercentage());
        }

        BigDecimal futureTotalWeight = currentTotalWeight.add(newWeight);

        if (futureTotalWeight.compareTo(new BigDecimal("100.00")) > 0) {
            throw new WeightLimitExceededException();
        }
    }

    @Transactional
    public void deleteProject(UUID projectId) {
        Project project = getProjectById(projectId);
        project.setDeletesAt(Instant.now());
        projectRepository.save(project);
    }

    @Transactional
    public ProjectItemResponse updateProjectItemConfig(UUID projectId, UUID itemId, UpdateProjectItemConfigRequest request) {
        ProjectItem item = projectItemRepository.findByProjectItemIdAndProject_ProjectId(itemId, projectId)
                .orElseThrow(ProjectItemNotFoundException::new);

        validateWeightLimit(projectId, request.getWeightPercentage(), itemId);

        projectItemMapper.updateItemConfigFromRequest(request, item);
        ProjectItem updatedItem = projectItemRepository.save(item);

        return projectItemMapper.toResponse(updatedItem);
    }

    @Transactional
    public void removeProjectItem(UUID projectId, UUID itemId) {
        ProjectItem item = projectItemRepository.findByProjectItemIdAndProject_ProjectId(itemId, projectId)
                .orElseThrow(ProjectItemNotFoundException::new);
        projectItemRepository.delete(item);
    }

    @Transactional
    public void changeProjectStatus(UUID projectId, ProjectStatus newStatus) {
        Project project = getProjectById(projectId);
        project.setOverallStatus(newStatus);
        projectRepository.save(project);
    }


}