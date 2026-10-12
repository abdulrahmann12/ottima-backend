package com.ottima.finishing_tracking.logging.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import com.ottima.finishing_tracking.logging.util.ClientInfoUtils;
import com.ottima.finishing_tracking.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityLoggingAspect {

    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "password", "passwordhash", "secret", "token", "refreshtoken",
            "currentpassword", "newpassword", "requestcode", "code", "authorization"
    );

    private final AuthenticatedUserService authenticatedUserService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Around("@annotation(logActivity)")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {

        String ipAddress = ClientInfoUtils.getClientIpAddress();
        String userAgent = ClientInfoUtils.getUserAgent();
        User currentUser = null;
        try {
            currentUser = authenticatedUserService.getCurrentUser();
        } catch (Exception e) {
            // Ignore for unauthenticated endpoints
        }

        Map<String, Object> inputData = extractArguments(joinPoint);

        try {
            Object result = joinPoint.proceed();

            Map<String, Object> outputData = null;

            if (result != null) {
                try {
                    outputData = new HashMap<>();
                    outputData.put("result", sanitizeValue(result));
                } catch (Exception ex) {
                    log.warn("Could not prepare output data for logging", ex);
                }
            }

            UserActivityLog successLog = UserActivityLog.builder()
                    .userId(currentUser != null ? currentUser.getUserId() : null)
                    .username(currentUser != null ? currentUser.getUsername() : "System")
                    .action(logActivity.actionType())
                    .status(ActivityStatus.SUCCESS)
                    .entityName(logActivity.entityName())
                    .oldValues(inputData)
                    .newValues(outputData)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .details(logActivity.details())
                    .build();


            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        rabbitTemplate.convertAndSend(RabbitConstants.LOGGING_EXCHANGE, RabbitConstants.ACTIVITY_LOG_KEY, successLog);
                    }
                });
            } else {

                rabbitTemplate.convertAndSend(RabbitConstants.LOGGING_EXCHANGE, RabbitConstants.ACTIVITY_LOG_KEY, successLog);
            }

            return result;

        } catch (Throwable e) {
            // Failure log
            UserActivityLog failedLog = UserActivityLog.builder()
                    .userId(currentUser != null ? currentUser.getUserId() : null)
                    .username(currentUser != null ? currentUser.getUsername() : "System")
                    .action(logActivity.actionType())
                    .status(ActivityStatus.FAILED)
                    .entityName(logActivity.entityName())
                    .oldValues(inputData)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .details(e.getMessage())
                    .build();

            try {
                rabbitTemplate.convertAndSend(RabbitConstants.LOGGING_EXCHANGE, RabbitConstants.ACTIVITY_LOG_KEY, failedLog);
            } catch (Exception rabbitEx) {
                log.error("Failed to publish failure log to RabbitMQ. Original error: {}", e.getMessage(), rabbitEx);
            }

            throw e;
        }
    }

    private Map<String, Object> extractArguments(ProceedingJoinPoint joinPoint) {
        Map<String, Object> argsMap = new HashMap<>();
        try {
            Object[] args = joinPoint.getArgs();
            CodeSignature signature = (CodeSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();

            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && !args[i].getClass().getName().startsWith("org.springframework.web.multipart")) {
                    String paramName = paramNames[i];
                    if (isSensitiveKey(paramName)) {
                        argsMap.put(paramName, "******");
                    } else {
                        argsMap.put(paramName, sanitizeValue(args[i]));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract arguments for logging", e);
        }
        return argsMap;
    }

    private Object sanitizeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> sanitizedMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                if (isSensitiveKey(key)) {
                    sanitizedMap.put(key, "******");
                } else {
                    sanitizedMap.put(key, sanitizeValue(entry.getValue()));
                }
            }
            return sanitizedMap;
        }
        if (value instanceof List<?> list) {
            return list.stream().map(this::sanitizeValue).toList();
        }
        String className = value.getClass().getName();
        if (className.startsWith("com.ottima") && !value.getClass().isEnum()) {
            try {
                Map<String, Object> map = objectMapper.convertValue(value, new TypeReference<Map<String, Object>>() {});
                return sanitizeValue(map);
            } catch (Exception ignored) {
                return value.toString();
            }
        }
        return value;
    }

    private boolean isSensitiveKey(String key) {
        if (key == null) return false;
        String normalized = key.toLowerCase().replace("_", "").replace("-", "");
        return SENSITIVE_KEYS.stream().anyMatch(normalized::contains);
    }
}