package com.ottima.finishing_tracking.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(
                // ── STATIC data: long TTL, rarely mutated ──
                buildCache("roles",          30, TimeUnit.MINUTES, 100),

                // ── MEDIUM volatility: normal TTL ──
                buildCache("users",          10, TimeUnit.MINUTES, 5_000),

                // ── SHORT TTL: high-frequency lookups that must stay fresh ──
                buildCache("userDetails",     2, TimeUnit.MINUTES, 1_000),
                buildCache("tokens",          2, TimeUnit.MINUTES, 5_000)
        ));
        return manager;
    }

    private CaffeineCache buildCache(String name, long duration, TimeUnit unit, long maxSize) {
        return new CaffeineCache(name,
                Caffeine.newBuilder()
                        .expireAfterWrite(duration, unit)
                        .maximumSize(maxSize)
                        .recordStats()
                        .build());
    }
}
