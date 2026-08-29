package com.ottima.finishing_tracking.notification.listener;

import com.ottima.finishing_tracking.notification.enums.ReferenceType;
import com.ottima.finishing_tracking.notification.event.*;
import com.ottima.finishing_tracking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleTicketCreated(TicketCreatedEvent event) {
        String roleEn = event.getSenderRole().contains("ADMIN") ? "Admin" : "Engineer";
        String title = "New Internal Ticket";
        String message = String.format("%s (%s) sent a new ticket: %s", roleEn, event.getSenderNameEn(), event.getTicketTitle());

        notificationService.createAndSendNotification(
                event.getReceiverId(), title, message, ReferenceType.TICKET, event.getTicketId()
        );
    }

    @Async
    @EventListener
    public void handleTicketStatusChanged(TicketStatusChangedEvent event) {
        String title = "Ticket Status Updated";
        String message = String.format("Your ticket (%s) status has been changed to: %s", event.getTicketTitle(), event.getNewStatus());

        notificationService.createAndSendNotification(
                event.getEngineerId(), title, message, ReferenceType.TICKET, event.getTicketId()
        );
    }

    @Async
    @EventListener
    public void handleCommentEvent(CommentEvent event) {
        String title = event.isReply() ? "New Reply to Your Comment" : "New Comment";
        String action = event.isReply() ? "replied to your comment" : "added a new comment";
        String message = String.format("%s %s on project: %s", event.getSenderName(), action, event.getProjectNameEn());

        notificationService.createAndSendNotification(
                event.getReceiverId(), title, message, ReferenceType.COMMENT, event.getCommentId()
        );
    }

    @Async
    @EventListener
    public void handleDailyUpdateStatusChanged(DailyUpdateStatusChangedEvent event) {
        String title = "Daily Update Status Changed";
        String statusAction = event.getNewStatus().equalsIgnoreCase("APPROVED") ? "approved" : "rejected";
        // Swapped the arguments to match English grammar naturally
        String message = String.format("The daily update for project %s has been %s", event.getProjectNameEn(), statusAction);

        notificationService.createAndSendNotification(
                event.getEngineerId(), title, message, ReferenceType.DAILY_UPDATE, event.getDailyUpdateId()
        );
    }

    @Async
    @EventListener
    public void handleDailyUpdateSubmitted(DailyUpdateSubmittedEvent event) {
        String title = "New Daily Update Submitted";
        String message = String.format("Engineer %s submitted a new daily update for project: %s",
                event.getEngineerName(),
                event.getProjectNameEn());

        notificationService.createAndSendNotification(
                event.getAdminId(), title, message, ReferenceType.DAILY_UPDATE, event.getDailyUpdateId()
        );
    }
}