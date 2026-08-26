package com.ottima.finishing_tracking.security;

public final class SecurityConstants {

    private SecurityConstants() {
        // لمنع إنشاء كائن من هذا الكلاس
    }

    public static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/regenerate-code",
            "/api/v1/auth/forget-password",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}