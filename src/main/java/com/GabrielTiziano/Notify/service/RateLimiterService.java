package com.GabrielTiziano.Notify.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final StringRedisTemplate stringRedisTemplate;

    public boolean isAllowed(String clientId){
        String key = "rate_limit:client: " + clientId;

        Long currentRequests = stringRedisTemplate.opsForValue().increment(key);

        if(currentRequests != null && currentRequests == 1){
            stringRedisTemplate.expire(key, Duration.ofMinutes(1));
        }

        return currentRequests != null && currentRequests <= 5;
    }
}
