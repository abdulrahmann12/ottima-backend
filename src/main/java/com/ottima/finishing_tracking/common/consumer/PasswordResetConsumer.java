package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.PasswordResetRequestedEvent;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.service.EmailService;
import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.exception.MailSendingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.PASSWORD_RESET_QUEUE)
    public void handlePasswordResetEvent(PasswordResetRequestedEvent event) {
        try {
            emailService.sendPasswordResetMail(event, Messages.RESET_PASSWORD);
        } catch (Exception e) {
            throw new MailSendingException(e);
        }
    }
}