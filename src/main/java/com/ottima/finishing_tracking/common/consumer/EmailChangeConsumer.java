package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.EmailChangeEvent;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.service.EmailService;
import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.exception.MailSendingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailChangeConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.USER_EMAIL_CHANGE_QUEUE)
    public void handleEmailChangeEvent(EmailChangeEvent event) {
        try {
            emailService.sendEmailChangeMail(event, Messages.MAIL_CHANGE);
        } catch (Exception e) {
            throw new MailSendingException(e);
        }
    }
}