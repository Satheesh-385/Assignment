package com.project.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BotGuardService {

    private final RedisTemplate<String, Object> redisTemplate;

    public BotGuardService(
            RedisTemplate<String, Object> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    // =========================================
    // HORIZONTAL CAP
    // Max 100 bot replies per post
    // =========================================
    public void checkHorizontalCap(Long postId) {

        String key = "post:" + postId + ":bot_count";

        Long count =
                redisTemplate.opsForValue().increment(key);

        if (count != null && count > 100) {

            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Bot reply limit exceeded for this post"
            );
        }
    }

    // =========================================
    // VERTICAL CAP
    // Max depth = 20
    // =========================================
    public void checkVerticalCap(int depthLevel) {

        if (depthLevel > 20) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Maximum comment depth exceeded"
            );
        }
    }

    // =========================================
    // COOLDOWN CAP
    // One interaction per 10 minutes
    // =========================================
    public void checkCooldown(
            Long botId,
            Long humanId) {

        String key =
                "cooldown:bot_" + botId +
                ":human_" + humanId;

        Boolean exists = redisTemplate.hasKey(key);

        if (Boolean.TRUE.equals(exists)) {

            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Bot is on cooldown for this human"
            );
        }

        // SET TTL = 10 MINUTES
        redisTemplate.opsForValue().set(
                key,
                "blocked",
                Duration.ofMinutes(10)
        );
    }
}