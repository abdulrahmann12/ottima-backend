package com.ottima.finishing_tracking.rate_limit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitConfig rateLimitConfig;
    private final ObjectMapper objectMapper;
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String ruleKey = resolveRule(request);
        if (ruleKey != null) {
            String clientIdentifier = resolveClientIdentifier(request);
            String bucketKey = ruleKey + "_" + clientIdentifier;
            RateLimitRule rule = rateLimitConfig.getRule(ruleKey);

            Bucket bucket = buckets.computeIfAbsent(bucketKey, k -> Bucket.builder()
                    .addLimit(limit -> limit.capacity(rule.limit()).refillGreedy(rule.limit(), rule.duration()))
                    .build());

            if (!bucket.tryConsume(1)) {
                sendRateLimitResponse(response, request);
                return; // Stop the request
            }
        }
        filterChain.doFilter(request, response);
    }

    // هنا بنربط مساراتنا الحقيقية بقواعد الـ Config اللي عملناها قبل كده
    private String resolveRule(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.equals("/api/v1/auth/login") && method.equals("POST")) return "LOGIN";
        if (path.equals("/api/v1/auth/forget-password") && method.equals("POST")) return "FORGOT_PASSWORD";
        if (path.equals("/api/v1/auth/reset-password") && method.equals("POST")) return "RESET_PASSWORD";
        if (path.equals("/api/v1/auth/regenerate-code") && method.equals("POST")) return "RESEND_OTP";

        if (path.startsWith("/api/v1/admins/dashboard") && method.equals("GET")) return "DASHBOARD";
        if (path.startsWith("/api/v1/users") || path.startsWith("/api/v1/clients") || path.startsWith("/api/v1/engineers")) return "USER_MANAGEMENT";

        return null; // لا توجد قاعدة، كمل عادي
    }

    private String resolveClientIdentifier(HttpServletRequest request) {
        // لو اليوزر عامل Login، نطبق الـ Rate Limit على الـ ID بتاعه
        if (request.getUserPrincipal() != null) {
            return "USER_" + request.getUserPrincipal().getName();
        }

        // استخدام getRemoteAddr هو الأأمن عشان نمنع الـ IP Spoofing
        return "IP_" + request.getRemoteAddr();
    }

    private void sendRateLimitResponse(HttpServletResponse response, HttpServletRequest request) throws IOException {
        BaseResponse error = new BaseResponse(Messages.TOO_MANY_REQUESTS, request.getRequestURI());
        response.setStatus(429); // Too Many Requests
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), error);
    }
}