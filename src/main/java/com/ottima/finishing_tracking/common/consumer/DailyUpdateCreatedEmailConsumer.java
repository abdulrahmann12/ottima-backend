package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.DailyUpdateCreatedEmailEvent;
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
public class DailyUpdateCreatedEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.DAILY_UPDATE_CREATED_QUEUE)
    public void handleDailyUpdateCreatedEvent(DailyUpdateCreatedEmailEvent event) {
        try {
            String subject = "New Daily Update Submitted: " + (event.getProjectNameEn() != null ? event.getProjectNameEn() : "Project");
            emailService.sendDailyUpdateCreatedMail(event, subject);
            log.info("Daily update email successfully sent to admin {}", event.getAdminEmail());
        } catch (Exception e) {
            log.error("Failed to process daily update email to admin {}", event.getAdminEmail(), e);
            throw new MailSendingException(e);
        }
    }
}
