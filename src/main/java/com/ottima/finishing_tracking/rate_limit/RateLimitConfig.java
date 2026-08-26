package com.ottima.finishing_tracking.rate_limit;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class RateLimitConfig {

    private final Map<String, RateLimitRule> rules = Map.of(
            "LOGIN", new RateLimitRule(5L, Duration.ofMinutes(1)),
            "FORGOT_PASSWORD", new RateLimitRule(3L, Duration.ofMinutes(15)),
            "RESET_PASSWORD", new RateLimitRule(5L, Duration.ofMinutes(15)),
            "RESEND_OTP", new RateLimitRule(3L, Duration.ofMinutes(5)),
            "USER_MANAGEMENT", new RateLimitRule(60L, Duration.ofMinutes(1)),
            "ROLE_MANAGEMENT", new RateLimitRule(60L, Duration.ofMinutes(1)),
            "TRACKING_OPERATIONS", new RateLimitRule(120L, Duration.ofMinutes(1)),
            "FILE_UPLOAD", new RateLimitRule(20L, Duration.ofMinutes(1)),
            "DASHBOARD", new RateLimitRule(60L, Duration.ofMinutes(1)),
            "EXPORT_REPORTS", new RateLimitRule(5L, Duration.ofMinutes(1))
    );

    public RateLimitRule getRule(String key) {
        return rules.getOrDefault(key, new RateLimitRule(100L, Duration.ofMinutes(1)));
    }
}