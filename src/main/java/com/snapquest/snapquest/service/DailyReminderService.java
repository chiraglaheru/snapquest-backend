package com.snapquest.snapquest.service;

import com.snapquest.snapquest.model.User;
import com.snapquest.snapquest.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyReminderService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public DailyReminderService(
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    @Scheduled(cron = "0 0 19 * * *")
    public void sendDailyReminder() {

        List<User> users =
                userRepository.findAll();

        for (User user : users) {

            if (
                    user.getExpoPushToken() != null
                            &&
                            !user.getExpoPushToken().isBlank()
            ) {

                notificationService.sendPush(
                        user.getExpoPushToken(),
                        "🎯Daily Reminder",
                        "You still have quests waiting for you."
                );
            }
        }

        System.out.println(
                "🎯Daily reminders sent"
        );
    }
}