package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.DailyUpdateEvaluatedEmailEvent;
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
public class DailyUpdateEvaluatedEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.DAILY_UPDATE_EVALUATED_QUEUE)
    public void handleDailyUpdateEvaluatedEvent(DailyUpdateEvaluatedEmailEvent event) {
        try {
            String subject = "Daily Update " + event.getStatus() + ": " + (event.getProjectNameEn() != null ? event.getProjectNameEn() : "Project");
            emailService.sendDailyUpdateEvaluatedMail(event, subject);
            log.info("Daily update evaluation email successfully sent to engineer {}", event.getEngineerEmail());
        } catch (Exception e) {
            log.error("Failed to process daily update evaluation email to engineer {}", event.getEngineerEmail(), e);
            throw new MailSendingException(e);
        }
    }
}
