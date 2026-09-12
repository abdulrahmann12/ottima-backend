package com.ottima.finishing_tracking.notification.service;

import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.exception.NotificationNotFoundException;
import com.ottima.finishing_tracking.exception.UnauthorizedActionException;
import com.ottima.finishing_tracking.notification.dto.response.NotificationResponse;
import com.ottima.finishing_tracking.notification.entity.Notification;
import com.ottima.finishing_tracking.notification.enums.ReferenceType;
import com.ottima.finishing_tracking.notification.mapper.NotificationMapper;
import com.ottima.finishing_tracking.notification.repository.NotificationRepository;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ottima.finishing_tracking.comment.entity.Comment;
import com.ottima.finishing_tracking.comment.repository.CommentRepository;
import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.daily_update.repository.DailyUpdateRepository;
import com.ottima.finishing_tracking.notification.dto.response.NotificationContextResponse;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.standard_item.entity.StandardItem;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final DailyUpdateRepository dailyUpdateRepository;
    private final ProjectItemRepository projectItemRepository;
    private final ProjectRepository projectRepository;

    // ==========================================
    // === Core Internal Logic (Called by Events)
    // ==========================================

    @Transactional
    public void createAndSendNotification(Long receiverId, String title, String message, ReferenceType type, UUID referenceId) {
        if (receiverId == null) {
            return;
        }

        Notification notification = Notification.builder()
                .userId(receiverId)
                .title(title)
                .message(message)
                .referenceType(type)
                .referenceId(referenceId)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse response = notificationMapper.toResponse(savedNotification);

        String destination = "/queue/notifications-" + receiverId;
        try {
            messagingTemplate.convertAndSend(destination, response);
            log.info("Real-time notification sent to user {} at destination {}", receiverId, destination);
        } catch (Exception e) {
            log.error("Failed to send real-time notification to user {}", receiverId, e);
        }
    }

    @Transactional
    public void createAndSendNotificationToAllAdmins(String title, String message, ReferenceType type, UUID referenceId) {
        List<Long> adminIds = userRepository.findActiveAdminUserIds();
        if (adminIds == null || adminIds.isEmpty()) {
            log.warn("No active admins found to receive notification: title={}, referenceId={}", title, referenceId);
            return;
        }
        createAndSendNotificationToMultipleUsers(adminIds, title, message, type, referenceId);
    }

    @Transactional
    public void createAndSendNotificationToMultipleUsers(List<Long> receiverIds, String title, String message, ReferenceType type, UUID referenceId) {
        if (receiverIds == null || receiverIds.isEmpty()) {
            return;
        }

        List<Notification> notifications = receiverIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(userId -> Notification.builder()
                        .userId(userId)
                        .title(title)
                        .message(message)
                        .referenceType(type)
                        .referenceId(referenceId)
                        .isRead(false)
                        .build())
                .toList();

        if (notifications.isEmpty()) {
            return;
        }

        List<Notification> savedNotifications = notificationRepository.saveAll(notifications);

        for (Notification notif : savedNotifications) {
            String destination = "/queue/notifications-" + notif.getUserId();
            try {
                NotificationResponse response = notificationMapper.toResponse(notif);
                messagingTemplate.convertAndSend(destination, response);
                log.info("Real-time notification sent to user {} at destination {}", notif.getUserId(), destination);
            } catch (Exception e) {
                log.error("Failed to send real-time notification to user {}", notif.getUserId(), e);
            }
        }
    }

    public Page<NotificationResponse> getMyNotifications(Pageable pageable) {
        User currentUser = authenticatedUserService.getCurrentUser();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getUserId(), pageable)
                .map(notificationMapper::toResponse);
    }

    public long getMyUnreadCount() {
        User currentUser = authenticatedUserService.getCurrentUser();
        return notificationRepository.countByUserIdAndIsReadFalse(currentUser.getUserId());
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();

        if (!notification.getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedActionException("You are not allowed to modify this notification.");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead() {
        User currentUser = authenticatedUserService.getCurrentUser();
        notificationRepository.markAllAsReadByUserId(currentUser.getUserId());
    }

    public NotificationContextResponse resolveContext(ReferenceType type, UUID referenceId) {
        if (type == null || referenceId == null) {
            return new NotificationContextResponse();
        }

        NotificationContextResponse.NotificationContextResponseBuilder builder = NotificationContextResponse.builder();

        switch (type) {
            case COMMENT -> {
                builder.commentId(referenceId);
                commentRepository.findById(referenceId).ifPresent(comment -> {
                    DailyUpdate du = comment.getDailyUpdate();
                    if (du != null) {
                        builder.dailyUpdateId(du.getDailyUpdateId());
                        ProjectItem pi = du.getProjectItem();
                        if (pi != null) {
                            builder.projectItemId(pi.getProjectItemId());
                            if (pi.getStandardItem() != null) {
                                builder.itemNameEn(pi.getStandardItem().getNameEn());
                                builder.itemNameAr(pi.getStandardItem().getNameAr());
                            }
                            Project pr = pi.getProject();
                            if (pr != null) {
                                builder.projectId(pr.getProjectId());
                                builder.projectNameEn(pr.getNameEn());
                                builder.projectNameAr(pr.getNameAr());
                            }
                        }
                    }
                });
            }
            case DAILY_UPDATE -> {
                builder.dailyUpdateId(referenceId);
                dailyUpdateRepository.findById(referenceId).ifPresent(du -> {
                    ProjectItem pi = du.getProjectItem();
                    if (pi != null) {
                        builder.projectItemId(pi.getProjectItemId());
                        if (pi.getStandardItem() != null) {
                            builder.itemNameEn(pi.getStandardItem().getNameEn());
                            builder.itemNameAr(pi.getStandardItem().getNameAr());
                        }
                        Project pr = pi.getProject();
                        if (pr != null) {
                            builder.projectId(pr.getProjectId());
                            builder.projectNameEn(pr.getNameEn());
                            builder.projectNameAr(pr.getNameAr());
                        }
                    }
                });
            }
            case TICKET -> {
                // Ticket context fallback lookup
                projectRepository.findById(referenceId).ifPresent(pr -> {
                    builder.projectId(pr.getProjectId());
                    builder.projectNameEn(pr.getNameEn());
                    builder.projectNameAr(pr.getNameAr());
                });
            }
            default -> {
                // If the referenceId points directly to a project item
                projectItemRepository.findById(referenceId).ifPresent(pi -> {
                    builder.projectItemId(pi.getProjectItemId());
                    if (pi.getStandardItem() != null) {
                        builder.itemNameEn(pi.getStandardItem().getNameEn());
                        builder.itemNameAr(pi.getStandardItem().getNameAr());
                    }
                    if (pi.getProject() != null) {
                        builder.projectId(pi.getProject().getProjectId());
                        builder.projectNameEn(pi.getProject().getNameEn());
                        builder.projectNameAr(pi.getProject().getNameAr());
                    }
                });
            }
        }

        return builder.build();
    }
}