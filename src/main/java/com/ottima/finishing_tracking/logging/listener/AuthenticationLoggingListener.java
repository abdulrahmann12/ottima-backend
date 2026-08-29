package com.ottima.finishing_tracking.logging.listener;

import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import com.ottima.finishing_tracking.logging.service.ActivityLogAsyncService;
import com.ottima.finishing_tracking.logging.util.ClientInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationLoggingListener {

    private final ActivityLogAsyncService logAsyncService;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            String ipAddress = ClientInfoUtils.getClientIpAddress();
            String userAgent = ClientInfoUtils.getUserAgent();

            UserActivityLog logEntry = UserActivityLog.builder()
                    .username(username)
                    .action(ActionType.LOGIN_SUCCESS)
                    .status(ActivityStatus.SUCCESS)
                    .entityName("Authentication")
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .details("User logged in successfully")
                    .build();

            logAsyncService.saveLog(logEntry);
        }
    }

    @EventListener
    public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String attemptedUsername = event.getAuthentication().getName();
        String errorMessage = event.getException().getMessage();

        String ipAddress = ClientInfoUtils.getClientIpAddress();
        String userAgent = ClientInfoUtils.getUserAgent();

        UserActivityLog logEntry = UserActivityLog.builder()
                .username(attemptedUsername != null ? attemptedUsername : "Unknown")
                .action(ActionType.LOGIN_FAILED)
                .status(ActivityStatus.FAILED)
                .entityName("Authentication")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .details("Login failed: " + errorMessage)
                .oldValues(Map.of("attempted_username", attemptedUsername))
                .build();

        logAsyncService.saveLog(logEntry);
    }
}