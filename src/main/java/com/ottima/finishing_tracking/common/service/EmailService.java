package com.ottima.finishing_tracking.common.service;

import com.ottima.finishing_tracking.common.events.CodeRegeneratedEvent;
import com.ottima.finishing_tracking.common.events.EmailChangeEvent;
import com.ottima.finishing_tracking.common.events.PasswordResetRequestedEvent;
import com.ottima.finishing_tracking.exception.MailSendingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TemplateEngine templateEngine;
    private final RestTemplate restTemplate;

    @Value("${app.brevo.api-key}")
    private String apiKey;

    @Value("${app.brevo.sender-email}")
    private String senderEmail;

    @Value("${app.brevo.sender-name}")
    private String senderName;

    public void sendPasswordResetMail(PasswordResetRequestedEvent event, String subject) {
        Context context = new Context();
        context.setVariable("name", event.getUsername());
        context.setVariable("code", event.getCode());

        sendEmail(event.getEmail(), subject, "emails/password-reset", context);
    }

    public void sendEmailChangeMail(EmailChangeEvent event, String subject) {
        Context context = new Context();
        context.setVariable("name", event.getUsername());
        context.setVariable("code", event.getCode());
        context.setVariable("newMail", event.getNewEmail());

        sendEmail(event.getOldEmail(), subject, "emails/change-email", context);
    }

    public void sendRegenerateCode(CodeRegeneratedEvent event, String subject) {
        Context context = new Context();
        context.setVariable("name", event.getUsername());
        context.setVariable("code", event.getCode());

        sendEmail(event.getEmail(), subject, "emails/send-code", context);
    }

    private void sendEmail(String to, String subject, String templatePath, Context context) {
        try {
            String htmlContent = templateEngine.process(templatePath, context);

            String url = "https://api.brevo.com/v3/smtp/email";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);
            headers.set("accept", "application/json");

            Map<String, Object> body = Map.of(
                    "sender", Map.of("name", senderName, "email", senderEmail),
                    "to", List.of(Map.of("email", to)),
                    "subject", subject,
                    "htmlContent", htmlContent
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);

        } catch (Exception e) {
            throw new MailSendingException();
        }
    }
}