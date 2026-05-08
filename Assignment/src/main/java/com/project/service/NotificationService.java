package com.project.service;

import java.time.Duration;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public NotificationService(
            RedisTemplate<String, Object> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    // =========================================
    // HANDLE BOT NOTIFICATION
    // =========================================

    public void handleBotNotification(
            Long userId,
            String message) {

        String cooldownKey =
                "user:" + userId + ":notif_cooldown";

        String pendingListKey =
                "user:" + userId + ":pending_notifs";

        // CHECK IF COOLDOWN EXISTS
        Boolean exists =
                redisTemplate.hasKey(cooldownKey);

        // =========================================
        // IF USER ALREADY RECEIVED NOTIFICATION
        // =========================================

        if (Boolean.TRUE.equals(exists)) {

            // STORE MESSAGE IN REDIS LIST
            redisTemplate.opsForList()
                    .rightPush(
                            pendingListKey,
                            message
                    );

            System.out.println(
                    "Notification batched for user "
                            + userId);

        } else {

            // SEND IMMEDIATE NOTIFICATION
            System.out.println(
                    "Push Notification Sent to User "
                            + userId
                            + ": "
                            + message);

            // SET 15 MINUTE COOLDOWN
            redisTemplate.opsForValue().set(
                    cooldownKey,
                    "ACTIVE",
                    Duration.ofMinutes(15)
            );
        }
    }

    // =========================================
    // GET ALL USERS WITH PENDING NOTIFICATIONS
    // =========================================

    public Set<String> getPendingNotificationKeys() {

        return redisTemplate.keys(
                "user:*:pending_notifs");
    }

    // =========================================
    // GET COUNT OF PENDING NOTIFICATIONS
    // =========================================

    public Long getNotificationCount(String key) {

        return redisTemplate.opsForList().size(key);
    }

    // =========================================
    // CLEAR NOTIFICATIONS
    // =========================================

    public void clearNotifications(String key) {

        redisTemplate.delete(key);
    }
}