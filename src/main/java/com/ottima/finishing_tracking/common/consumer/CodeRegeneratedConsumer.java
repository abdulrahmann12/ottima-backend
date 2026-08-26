package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.common.events.CodeRegeneratedEvent;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.service.EmailService;
import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.exception.MailSendingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodeRegeneratedConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitConstants.CODE_REGENERATED_QUEUE)
    public void handleCodeRegeneratedEvent(CodeRegeneratedEvent event) {
        try{
            emailService.sendRegenerateCode(event, Messages.RESEND_CODE);
        }
        catch (Exception e){
            throw new MailSendingException(e);
        }

    }
}