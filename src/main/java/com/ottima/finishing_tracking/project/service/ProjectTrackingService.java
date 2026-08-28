package com.ottima.finishing_tracking.project.service;

import com.ottima.finishing_tracking.exception.ProjectItemNotFoundException;
import com.ottima.finishing_tracking.project.dto.request.UpdateItemProgressRequest;
import com.ottima.finishing_tracking.project.dto.response.ProjectItemResponse;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.project.mapper.ProjectItemMapper;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectTrackingService {

    private final ProjectItemRepository projectItemRepository;
    private final ProjectItemMapper projectItemMapper;

    @Transactional
    public ProjectItemResponse updateItemProgress(UUID projectId, UUID itemId, UpdateItemProgressRequest request) {

        ProjectItem item = projectItemRepository.findByProjectItemIdAndProject_ProjectId(itemId, projectId)
                .orElseThrow(ProjectItemNotFoundException::new);

        item.setCompletionPercentage(request.getCompletionPercentage());
        if (request.getCompletionPercentage().compareTo(java.math.BigDecimal.ZERO) == 0) {
            item.setStatus(com.ottima.finishing_tracking.project.enums.ProjectItemStatus.PENDING);
        } else if (request.getCompletionPercentage().compareTo(new java.math.BigDecimal("100.0")) >= 0) {
            item.setStatus(com.ottima.finishing_tracking.project.enums.ProjectItemStatus.COMPLETED);
        } else {
                item.setStatus(com.ottima.finishing_tracking.project.enums.ProjectItemStatus.IN_PROGRESS);
        }

        ProjectItem updatedItem = projectItemRepository.save(item);

        return projectItemMapper.toResponse(updatedItem);
    }
}