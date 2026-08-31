package com.ottima.finishing_tracking.daily_update.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.daily_update.dto.request.CreateDailyUpdateRequest;
import com.ottima.finishing_tracking.daily_update.dto.request.EvaluateDailyUpdateRequest;
import com.ottima.finishing_tracking.daily_update.dto.response.DailyUpdateResponse;
import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import com.ottima.finishing_tracking.daily_update.mapper.DailyUpdateMapper;
import com.ottima.finishing_tracking.daily_update.repository.DailyUpdateRepository;
import com.ottima.finishing_tracking.exception.AdminNotFoundException;
import com.ottima.finishing_tracking.exception.DailyUpdateNotFoundException;
import com.ottima.finishing_tracking.exception.ProjectAccessDeniedException;
import com.ottima.finishing_tracking.exception.ProjectItemNotFoundException;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import com.ottima.finishing_tracking.notification.event.DailyUpdateStatusChangedEvent;
import com.ottima.finishing_tracking.notification.event.DailyUpdateSubmittedEvent;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class DailyUpdateService {

    private final DailyUpdateRepository dailyUpdateRepository;
    private final ProjectItemRepository projectItemRepository;
    private final UserRepository userRepository;
    private final DailyUpdateMapper dailyUpdateMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final ApplicationEventPublisher eventPublisher;

    @LogActivity(actionType = ActionType.CREATE, entityName = Constants.DAILY_UPDATE_ENTITY, details = Messages.DAILY_UPDATE_CREATED_LOG)
    @Transactional
    @CacheEvict(value = "projectUpdates", allEntries = true)
    public DailyUpdateResponse createDailyUpdate(UUID projectId, @Valid CreateDailyUpdateRequest request) {
        ProjectItem projectItem = projectItemRepository.findByProjectItemIdAndProject_ProjectId(request.getProjectItemId(), projectId)
                .orElseThrow(ProjectItemNotFoundException::new);

        User currentEngineer = authenticatedUserService.getCurrentUser();

        DailyUpdate dailyUpdate = dailyUpdateMapper.toEntity(request);

        dailyUpdate.setProjectItem(projectItem);
        dailyUpdate.setEngineer(currentEngineer);

        DailyUpdate savedUpdate = dailyUpdateRepository.save(dailyUpdate);

        Long targetAdminId = userRepository
                .findFirstActiveByRole_RoleName("ADMIN")
                .orElseThrow(AdminNotFoundException::new)
                .getUserId();

        eventPublisher.publishEvent(DailyUpdateSubmittedEvent.builder()
                .dailyUpdateId(savedUpdate.getDailyUpdateId())
                .adminId(targetAdminId)
                .engineerName(currentEngineer.getUsername())
                .projectNameEn(savedUpdate.getProjectItem().getProject().getNameEn())
                .projectNameAr(savedUpdate.getProjectItem().getProject().getNameAr())
                .build());

        return dailyUpdateMapper.toResponse(savedUpdate,false);
    }

    @Cacheable(value = "projectUpdates", key = "'admin-' + #projectId + '-' + #projectItemId + '-' + #engineerId + '-' + #status + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<DailyUpdateResponse> getUpdatesForAdmin(
            UUID projectId,
            UUID projectItemId,
            Long engineerId,
            UpdateStatus status,
            Pageable pageable) {

        return dailyUpdateRepository.findFilteredUpdates(
                projectId, projectItemId, engineerId, status, pageable
        ).map(update -> dailyUpdateMapper.toResponse(update, false));
    }

    @Cacheable(value = "projectUpdates", key = "'engineer-' + #root.target.getCurrentUserId() + '-' + #projectId + '-' + #projectItemId + '-' + #status + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<DailyUpdateResponse> getMyUpdatesAsEngineer(
            UUID projectId,
            UUID projectItemId,
            UpdateStatus status,
            Pageable pageable) {

        Long currentEngineerId = authenticatedUserService.getCurrentUser().getUserId();

        return dailyUpdateRepository.findFilteredUpdates(
                projectId, projectItemId, currentEngineerId, status, pageable
        ).map(update -> dailyUpdateMapper.toResponse(update, false));
    }

    @Cacheable(value = "projectUpdates", key = "'client-' + #root.target.getCurrentUserId() + '-' + #projectItemId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<DailyUpdateResponse> getUpdatesForClient(UUID projectItemId, Pageable pageable) {
        Long currentClientId = authenticatedUserService.getCurrentUser().getUserId();

        ProjectItem projectItem = projectItemRepository.findByIdWithProjectAndClient(projectItemId)
                .orElseThrow(ProjectItemNotFoundException::new);

        if (!projectItem.getProject().getClient().getUserId().equals(currentClientId)) {
            throw new ProjectAccessDeniedException();
        }

        return dailyUpdateRepository.findAllByProjectItem_ProjectItemIdAndStatusOrderByCreatedAtDesc(
                projectItemId, UpdateStatus.APPROVED, pageable
        ).map(update -> dailyUpdateMapper.toResponse(update, true));
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.DAILY_UPDATE_ENTITY, details = Messages.DAILY_UPDATE_EVALUATED_LOG)
    @Transactional
    @CacheEvict(value = "projectUpdates", allEntries = true)
    public DailyUpdateResponse evaluateDailyUpdate(UUID dailyUpdateId, @Valid EvaluateDailyUpdateRequest request) {

        DailyUpdate dailyUpdate = dailyUpdateRepository.findById(dailyUpdateId)
                .orElseThrow(DailyUpdateNotFoundException::new);

        dailyUpdate.setStatus(request.getStatus());
        dailyUpdate.setNotes(request.getNotes());
        dailyUpdate.setTitle(request.getTitle());

        User currentAdmin = authenticatedUserService.getCurrentUser();
        dailyUpdate.setApprovedByAdmin(currentAdmin);

        if (request.getImageEvaluations() != null && dailyUpdate.getImages() != null) {
            for (EvaluateDailyUpdateRequest.ImageEvaluation eval : request.getImageEvaluations()) {
                dailyUpdate.getImages().stream()
                        .filter(img -> img.getUpdateImageId().equals(eval.getUpdateImageId()))
                        .findFirst()
                        .ifPresent(img -> img.setApproved(eval.getApproved()));
            }
        }
        DailyUpdate savedUpdate = dailyUpdateRepository.save(dailyUpdate);

        eventPublisher.publishEvent(DailyUpdateStatusChangedEvent.builder()
                .dailyUpdateId(savedUpdate.getDailyUpdateId())
                .engineerId(savedUpdate.getEngineer().getUserId())
                .projectNameAr(savedUpdate.getProjectItem().getProject().getNameAr())
                .projectNameEn(savedUpdate.getProjectItem().getProject().getNameEn())
                .newStatus(savedUpdate.getStatus().name())
                .build());

        return dailyUpdateMapper.toResponse(savedUpdate,false);
    }

    public Long getCurrentUserId() {
        return authenticatedUserService.getCurrentUser().getUserId();
    }
}