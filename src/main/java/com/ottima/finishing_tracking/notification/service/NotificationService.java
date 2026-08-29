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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final SimpMessagingTemplate messagingTemplate;

    // ==========================================
    // === Core Internal Logic (Called by Events)
    // ==========================================

    @Transactional
    public void createAndSendNotification(Long receiverId, String title, String message, ReferenceType type, UUID referenceId) {
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
}