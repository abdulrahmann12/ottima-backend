package com.ottima.finishing_tracking.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@ConfigurationProperties(prefix = "jwt.refresh-token")
@Getter
@Setter
public class RefreshTokenProperties {

    @DurationUnit(ChronoUnit.MINUTES)
    private Duration expirationMinutes;
}