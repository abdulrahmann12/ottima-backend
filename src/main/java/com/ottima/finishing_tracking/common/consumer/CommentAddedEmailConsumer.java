package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.CommentAddedEmailEvent;
import com.ottima.finishing_tracking.common.service.EmailService;
import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.exception.MailSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentAddedEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.COMMENT_ADDED_QUEUE)
    public void handleCommentAddedEvent(CommentAddedEmailEvent event) {
        try {
            String subject = "New Client Comment on Project: " + (event.getProjectNameEn() != null ? event.getProjectNameEn() : "Project");
            emailService.sendCommentAddedMail(event, subject);
            log.info("Comment added email successfully sent to admin {}", event.getAdminEmail());
        } catch (Exception e) {
            log.error("Failed to process comment added email to admin {}", event.getAdminEmail(), e);
            throw new MailSendingException(e);
        }
    }
}
