package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.TicketCreatedEmailEvent;
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
public class TicketCreatedEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.TICKET_CREATED_QUEUE)
    public void handleTicketCreatedEmailEvent(TicketCreatedEmailEvent event) {
        try {
            String proj = event.getProjectNameEn() != null ? event.getProjectNameEn() : (event.getProjectNameAr() != null ? event.getProjectNameAr() : "Project");
            String subject = String.format("New Internal Request: %s (%s)", event.getTicketTitle(), proj);
            emailService.sendTicketCreatedMail(event, subject);
            log.info("Ticket created email notification successfully sent to receiver {}", event.getReceiverEmail());
        } catch (Exception e) {
            log.error("Failed to process ticket created email to receiver {}", event.getReceiverEmail(), e);
            throw new MailSendingException(e);
        }
    }
}
