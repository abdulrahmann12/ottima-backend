package com.ottima.finishing_tracking.logging.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import com.ottima.finishing_tracking.logging.service.ActivityLogAsyncService;
import com.ottima.finishing_tracking.logging.util.ClientInfoUtils;
import com.ottima.finishing_tracking.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityLoggingAspect {

    private final ActivityLogAsyncService logAsyncService;
    private final AuthenticatedUserService authenticatedUserService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(logActivity)")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {

        String ipAddress = ClientInfoUtils.getClientIpAddress();
        String userAgent = ClientInfoUtils.getUserAgent();
        User currentUser = null;
        try {
            currentUser = authenticatedUserService.getCurrentUser();
        } catch (Exception e) {
        }

        Map<String, Object> inputData = extractArguments(joinPoint);

        Object result = null;
        ActivityStatus status = ActivityStatus.SUCCESS;
        String errorDetails = null;

        try {
            result = joinPoint.proceed();
            return result;

        } catch (Exception e) {
            status = ActivityStatus.FAILED;
            errorDetails = e.getMessage();
            throw e;

        } finally {
            Map<String, Object> outputData = null;
            if (result != null && status == ActivityStatus.SUCCESS) {
                try {
                    outputData = objectMapper.convertValue(result, new TypeReference<Map<String, Object>>() {});
                } catch (Exception ex) {
                    log.warn("Could not serialize output data for logging", ex);
                }
            }

            UserActivityLog activityLog = UserActivityLog.builder()
                    .userId(currentUser != null ? currentUser.getUserId() : null)
                    .username(currentUser != null ? currentUser.getUsername() : "System")
                    .action(logActivity.actionType())
                    .status(status)
                    .entityName(logActivity.entityName())
                    .oldValues(inputData)
                    .newValues(outputData)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .details(errorDetails != null ? errorDetails : logActivity.details())
                    .build();

            logAsyncService.saveLog(activityLog);
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
                    argsMap.put(paramNames[i], args[i]);
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract arguments for logging", e);
        }
        return argsMap;
    }
}