package com.ottima.finishing_tracking.comment.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.comment.dto.request.AddCommentRequest;
import com.ottima.finishing_tracking.comment.dto.request.EditCommentRequest;
import com.ottima.finishing_tracking.comment.dto.request.ReplyCommentRequest;
import com.ottima.finishing_tracking.comment.dto.response.CommentContextResponse;
import com.ottima.finishing_tracking.comment.dto.response.CommentResponse;
import com.ottima.finishing_tracking.comment.entity.Comment;
import com.ottima.finishing_tracking.comment.mapper.CommentMapper;
import com.ottima.finishing_tracking.comment.repository.CommentRepository;
import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import com.ottima.finishing_tracking.daily_update.repository.DailyUpdateRepository;
import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.notification.event.CommentEvent;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.ottima.finishing_tracking.common.events.CommentAddedEmailEvent;
import com.ottima.finishing_tracking.common.events.CommentRepliedEmailEvent;
import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Validated
public class CommentService {

    private final CommentRepository commentRepository;
    private final DailyUpdateRepository dailyUpdateRepository;
    private final CommentMapper commentMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final ApplicationEventPublisher eventPublisher;
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;

    @LogActivity(actionType = ActionType.CREATE, entityName = Constants.COMMENT_ENTITY, details = Messages.COMMENT_ADDED_LOG)
    @Transactional
    @CacheEvict(value = "updateComments", allEntries = true)
    public CommentResponse addComment(UUID dailyUpdateId, @Valid AddCommentRequest request) {
        DailyUpdate dailyUpdate = dailyUpdateRepository.findById(dailyUpdateId)
                .orElseThrow(DailyUpdateNotFoundException::new);

        User currentClient = authenticatedUserService.getCurrentUser();

        if (dailyUpdate.getStatus() != UpdateStatus.APPROVED) {
            throw new UnapprovedDailyUpdateCommentException();
        }

        Long projectOwnerId = dailyUpdate.getProjectItem().getProject().getClient().getUserId();
        if (!projectOwnerId.equals(currentClient.getUserId())) {
            throw new ProjectAccessDeniedException();
        }

        Comment comment = Comment.builder()
                .dailyUpdate(dailyUpdate)
                .client(currentClient)
                .clientComment(request.getClientComment())
                .build();

        Comment savedComment = commentRepository.save(comment);

        if (dailyUpdate.getApprovedByAdmin() != null) {
            eventPublisher.publishEvent(CommentEvent.builder()
                    .commentId(savedComment.getId())
                    .receiverId(dailyUpdate.getApprovedByAdmin().getUserId())
                    .senderName(currentClient.getUsername())
                    .projectNameEn(dailyUpdate.getProjectItem().getProject().getNameEn())
                    .projectNameAr(dailyUpdate.getProjectItem().getProject().getNameAr())
                    .isReply(false)
                    .build());
        }

        // Publish email notification to all active admins via RabbitMQ
        try {
            List<User> activeAdmins = userRepository.findFirstActiveByRole_RoleName("ADMIN");
            if (activeAdmins != null && !activeAdmins.isEmpty()) {
                String clientDisplayName = currentClient.getFullNameEn() != null ? currentClient.getFullNameEn() : currentClient.getUsername();
                String stdItemNameEn = dailyUpdate.getProjectItem().getStandardItem() != null ? dailyUpdate.getProjectItem().getStandardItem().getNameEn() : "";
                String stdItemNameAr = dailyUpdate.getProjectItem().getStandardItem() != null ? dailyUpdate.getProjectItem().getStandardItem().getNameAr() : "";

                for (User admin : activeAdmins) {
                    if (admin.getEmail() != null && !admin.getEmail().isBlank()) {
                        CommentAddedEmailEvent emailEvent = CommentAddedEmailEvent.builder()
                                .adminEmail(admin.getEmail())
                                .adminName(admin.getFullNameEn() != null ? admin.getFullNameEn() : admin.getUsername())
                                .clientName(clientDisplayName)
                                .projectNameEn(dailyUpdate.getProjectItem().getProject().getNameEn())
                                .projectNameAr(dailyUpdate.getProjectItem().getProject().getNameAr())
                                .itemNameEn(stdItemNameEn)
                                .itemNameAr(stdItemNameAr)
                                .commentText(savedComment.getClientComment())
                                .timestamp(Instant.now())
                                .build();

                        rabbitTemplate.convertAndSend(RabbitConstants.NOTIFICATION_EXCHANGE, RabbitConstants.COMMENT_ADDED_KEY, emailEvent);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to publish CommentAddedEmailEvent via RabbitMQ for comment {}", savedComment.getId(), e);
        }

        return commentMapper.toResponse(savedComment);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.COMMENT_ENTITY, details = Messages.COMMENT_REPLIED_LOG)
    @Transactional
    @CacheEvict(value = "updateComments", allEntries = true)
    public CommentResponse replyToComment(UUID commentId, ReplyCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User currentAdmin = authenticatedUserService.getCurrentUser();

        comment.setAdminReply(request.getAdminReply());
        comment.setRepliedByAdmin(currentAdmin);
        comment.setRepliedAt(Instant.now());

        Comment savedComment = commentRepository.save(comment);

        eventPublisher.publishEvent(CommentEvent.builder()
                .commentId(savedComment.getId())
                .receiverId(savedComment.getClient().getUserId())
                .senderName(currentAdmin.getUsername())
                .projectNameAr(savedComment.getDailyUpdate().getProjectItem().getProject().getNameAr())
                .projectNameEn(savedComment.getDailyUpdate().getProjectItem().getProject().getNameEn())
                .isReply(true)
                .build());

        // Publish email notification to client via RabbitMQ
        try {
            User client = savedComment.getClient();
            if (client != null && client.getEmail() != null && !client.getEmail().isBlank()) {
                String clientDisplayName = client.getFullNameEn() != null ? client.getFullNameEn() : client.getUsername();
                String adminDisplayName = currentAdmin.getFullNameEn() != null ? currentAdmin.getFullNameEn() : currentAdmin.getUsername();

                CommentRepliedEmailEvent emailEvent = CommentRepliedEmailEvent.builder()
                        .clientEmail(client.getEmail())
                        .clientName(clientDisplayName)
                        .adminName(adminDisplayName)
                        .projectNameEn(savedComment.getDailyUpdate().getProjectItem().getProject().getNameEn())
                        .projectNameAr(savedComment.getDailyUpdate().getProjectItem().getProject().getNameAr())
                        .clientComment(savedComment.getClientComment())
                        .adminReply(savedComment.getAdminReply())
                        .timestamp(Instant.now())
                        .build();

                rabbitTemplate.convertAndSend(RabbitConstants.NOTIFICATION_EXCHANGE, RabbitConstants.COMMENT_REPLIED_KEY, emailEvent);
            }
        } catch (Exception e) {
            log.error("Failed to publish CommentRepliedEmailEvent via RabbitMQ for comment {}", savedComment.getId(), e);
        }

        return commentMapper.toResponse(savedComment);
    }

    @Cacheable(value = "updateComments", key = "'comments-' + #root.target.getCurrentUserId() + '-' + #dailyUpdateId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CommentResponse> getCommentsForUpdate(UUID dailyUpdateId, Pageable pageable) {
        return commentRepository.findByDailyUpdate_DailyUpdateIdOrderByCreatedAtDesc(dailyUpdateId, pageable)
                .map(commentMapper::toResponse);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.COMMENT_ENTITY, details = Messages.COMMENT_UPDATED_LOG)
    @Transactional
    @CacheEvict(value = "updateComments", allEntries = true)
    public CommentResponse editComment(UUID commentId, @Valid EditCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User currentClient = authenticatedUserService.getCurrentUser();

        if (!comment.getClient().getUserId().equals(currentClient.getUserId())) {
            throw new CommentAccessDeniedException();
        }

        if (comment.getAdminReply() != null) {
            throw new CommentAlreadyRepliedException();
        }

        comment.setClientComment(request.getClientComment());

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @LogActivity(actionType = ActionType.DELETE, entityName = Constants.COMMENT_ENTITY, details = Messages.COMMENT_DELETED_LOG)
    @Transactional
    @CacheEvict(value = "updateComments", allEntries = true)
    public void deleteComment(UUID commentId, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!isAdmin) {
            User currentClient = authenticatedUserService.getCurrentUser();
            if (!comment.getClient().getUserId().equals(currentClient.getUserId())) {
                throw new CommentAccessDeniedException();
            }
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public CommentContextResponse resolveCommentContext(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();
        String roleName = currentUser.getRole().getRoleName();

        if ("CLIENT".equals(roleName)) {
            Long projectOwnerId = comment.getDailyUpdate().getProjectItem().getProject().getClient().getUserId();
            if (!projectOwnerId.equals(currentUser.getUserId())) {
                throw new CommentAccessDeniedException();
            }
        }

        var dailyUpdate = comment.getDailyUpdate();
        var projectItem = dailyUpdate.getProjectItem();
        var project = projectItem.getProject();
        var standardItem = projectItem.getStandardItem();

        return CommentContextResponse.builder()
                .commentId(comment.getId())
                .dailyUpdateId(dailyUpdate.getDailyUpdateId())
                .projectItemId(projectItem.getProjectItemId())
                .projectId(project.getProjectId())
                .projectNameAr(project.getNameAr())
                .projectNameEn(project.getNameEn())
                .itemNameAr(standardItem != null ? standardItem.getNameAr() : "")
                .itemNameEn(standardItem != null ? standardItem.getNameEn() : "")
                .build();
    }

    public Long getCurrentUserId() {
        return authenticatedUserService.getCurrentUser().getUserId();
    }
}