package com.project.scheduler;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.service.NotificationService;

@Component
public class NotificationScheduler {

    private final NotificationService notificationService;

    public NotificationScheduler(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    // RUN EVERY 5 MINUTES
    @Scheduled(fixedRate = 300000)

    public void sweepNotifications() {

        System.out.println(
                "Running Notification Sweeper...");

        Set<String> keys =
                notificationService
                        .getPendingNotificationKeys();

        if (keys == null || keys.isEmpty()) {

            System.out.println(
                    "No pending notifications");

            return;
        }

        for (String key : keys) {

            Long count =
                    notificationService
                            .getNotificationCount(key);

            if (count != null && count > 0) {

                System.out.println(
                        "Summarized Push Notification: "
                                + "Bot X and "
                                + count
                                + " others interacted with your posts."
                );

                // CLEAR REDIS LIST
                notificationService
                        .clearNotifications(key);
            }
        }
    }
}