package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.CommentRepliedEmailEvent;
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
public class CommentRepliedEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.COMMENT_REPLIED_QUEUE)
    public void handleCommentRepliedEvent(CommentRepliedEmailEvent event) {
        try {
            String subject = "Official Reply to Your Comment on Project: " + (event.getProjectNameEn() != null ? event.getProjectNameEn() : "Project");
            emailService.sendCommentRepliedMail(event, subject);
            log.info("Comment reply email successfully sent to client {}", event.getClientEmail());
        } catch (Exception e) {
            log.error("Failed to process comment reply email to client {}", event.getClientEmail(), e);
            throw new MailSendingException(e);
        }
    }
}
