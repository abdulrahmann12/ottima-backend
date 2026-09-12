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

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TemplateEngine templateEngine;
    private final RestTemplate restTemplate;

    private static final ZoneId CAIRO_ZONE = ZoneId.of("Africa/Cairo");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.ENGLISH);

    @Value("${app.brevo.api-key}")
    private String apiKey;

    @Value("${app.brevo.sender-email}")
    private String senderEmail;

    @Value("${app.brevo.sender-name}")
    private String senderName;

    private String formatDateTime(Instant instant) {
        if (instant == null) {
            instant = Instant.now();
        }
        try {
            return FORMATTER.withZone(ZoneId.systemDefault()).format(instant);
        } catch (Exception e) {
            return FORMATTER.withZone(CAIRO_ZONE).format(instant);
        }
    }

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

    public void sendDailyUpdateCreatedMail(com.ottima.finishing_tracking.common.events.DailyUpdateCreatedEmailEvent event, String subject) {
        Context context = new Context();
        context.setVariable("adminName", event.getAdminName() != null ? event.getAdminName() : "Admin");
        context.setVariable("engineerName", event.getEngineerName() != null ? event.getEngineerName() : "Engineer");
        context.setVariable("projectName", event.getProjectNameEn() != null ? event.getProjectNameEn() : (event.getProjectNameAr() != null ? event.getProjectNameAr() : "Project"));
        context.setVariable("itemName", event.getItemNameEn() != null ? event.getItemNameEn() : (event.getItemNameAr() != null ? event.getItemNameAr() : "Project Item"));
        context.setVariable("updateTitle", event.getUpdateTitle());
        context.setVariable("notes", event.getNotes());
        context.setVariable("createdAt", formatDateTime(event.getTimestamp()));

        sendEmail(event.getAdminEmail(), subject, "emails/daily-update-created", context);
    }

    public void sendDailyUpdateEvaluatedMail(com.ottima.finishing_tracking.common.events.DailyUpdateEvaluatedEmailEvent event, String subject) {
        Context context = new Context();
        context.setVariable("engineerName", event.getEngineerName() != null ? event.getEngineerName() : "Engineer");
        context.setVariable("adminName", event.getAdminName() != null ? event.getAdminName() : "Admin");
        context.setVariable("projectName", event.getProjectNameEn() != null ? event.getProjectNameEn() : (event.getProjectNameAr() != null ? event.getProjectNameAr() : "Project"));
        context.setVariable("itemName", event.getItemNameEn() != null ? event.getItemNameEn() : (event.getItemNameAr() != null ? event.getItemNameAr() : "Project Item"));
        context.setVariable("updateTitle", event.getUpdateTitle());
        context.setVariable("status", event.getStatus() != null ? event.getStatus() : "EVALUATED");
        context.setVariable("notes", event.getNotes());
        context.setVariable("evaluatedAt", formatDateTime(event.getTimestamp()));

        sendEmail(event.getEngineerEmail(), subject, "emails/daily-update-evaluated", context);
    }

    public void sendCommentAddedMail(com.ottima.finishing_tracking.common.events.CommentAddedEmailEvent event, String subject) {
        Context context = new Context();
        context.setVariable("adminName", event.getAdminName() != null ? event.getAdminName() : "Admin");
        context.setVariable("clientName", event.getClientName() != null ? event.getClientName() : "Client");
        context.setVariable("projectName", event.getProjectNameEn() != null ? event.getProjectNameEn() : (event.getProjectNameAr() != null ? event.getProjectNameAr() : "Project"));
        context.setVariable("itemName", event.getItemNameEn() != null ? event.getItemNameEn() : (event.getItemNameAr() != null ? event.getItemNameAr() : "Project Item"));
        context.setVariable("commentText", event.getCommentText());
        context.setVariable("createdAt", formatDateTime(event.getTimestamp()));

        sendEmail(event.getAdminEmail(), subject, "emails/comment-added", context);
    }

    public void sendCommentRepliedMail(com.ottima.finishing_tracking.common.events.CommentRepliedEmailEvent event, String subject) {
        Context context = new Context();
        context.setVariable("clientName", event.getClientName() != null ? event.getClientName() : "Client");
        context.setVariable("adminName", event.getAdminName() != null ? event.getAdminName() : "Admin");
        context.setVariable("projectName", event.getProjectNameEn() != null ? event.getProjectNameEn() : (event.getProjectNameAr() != null ? event.getProjectNameAr() : "Project"));
        context.setVariable("clientComment", event.getClientComment());
        context.setVariable("adminReply", event.getAdminReply());
        context.setVariable("repliedAt", formatDateTime(event.getTimestamp()));

        sendEmail(event.getClientEmail(), subject, "emails/comment-replied", context);
    }

    public void sendTicketCreatedMail(com.ottima.finishing_tracking.common.events.TicketCreatedEmailEvent event, String subject) {
        Context context = new Context();
        context.setVariable("receiverName", event.getReceiverName() != null ? event.getReceiverName() : "User");
        context.setVariable("senderName", event.getSenderName() != null ? event.getSenderName() : "User");
        context.setVariable("senderRole", event.getSenderRole() != null ? event.getSenderRole() : "User");
        context.setVariable("projectName", event.getProjectNameEn() != null ? event.getProjectNameEn() : (event.getProjectNameAr() != null ? event.getProjectNameAr() : "Project"));
        context.setVariable("ticketType", event.getTicketType() != null ? event.getTicketType().replace("_", " ") : "Internal Request");
        context.setVariable("ticketTitle", event.getTicketTitle());
        context.setVariable("description", event.getDescription());
        context.setVariable("amount", event.getAmount());
        context.setVariable("createdAt", formatDateTime(event.getTimestamp()));

        sendEmail(event.getReceiverEmail(), subject, "emails/ticket-created", context);
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