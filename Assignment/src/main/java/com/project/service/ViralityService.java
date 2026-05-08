package com.project.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.project.enums.InteractionType;

@Service
public class ViralityService {

    private final RedisTemplate<String, Object> redisTemplate;

    public ViralityService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // UPDATE VIRALITY SCORE
    public void updateViralityScore(
            Long postId,
            InteractionType type) {

        String key = "post:" + postId + ":virality_score";

        int points = switch (type) {

            case HUMAN_LIKE -> 20;

            case HUMAN_COMMENT -> 50;

            case BOT_REPLY -> 1;
        };

        redisTemplate.opsForValue().increment(key, points);
    }

    // GET CURRENT SCORE
    public Long getViralityScore(Long postId) {

        String key = "post:" + postId + ":virality_score";

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return 0L;
        }

        return Long.parseLong(value.toString());
    }

    // REDIS LOCK
    public boolean acquireLock(Long postId) {

        String lockKey = "lock:post:" + postId;

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(
                        lockKey,
                        "LOCKED",
                        Duration.ofSeconds(30));

        return Boolean.TRUE.equals(success);
    }

    // RELEASE LOCK
    public void releaseLock(Long postId) {

        String lockKey = "lock:post:" + postId;

        redisTemplate.delete(lockKey);
    }
}