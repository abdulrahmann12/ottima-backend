package com.ottima.finishing_tracking.rate_limit;

import java.time.Duration;

public record RateLimitRule (
        long limit,
        Duration duration
) {}