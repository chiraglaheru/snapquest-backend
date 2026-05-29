package com.snapquest.snapquest.service;

import com.snapquest.snapquest.model.Quest;
import com.snapquest.snapquest.repository.QuestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.snapquest.snapquest.model.User;
import com.snapquest.snapquest.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class QuestRotationService {

    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public QuestRotationService(
            QuestRepository questRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 3600000)
    public void rotateQuests() {

        List<Quest> allQuests =
                questRepository.findByActiveTrue();

        allQuests.forEach(q ->
                q.setFeatured(false));

        Collections.shuffle(allQuests);

        int count =
                Math.min(5, allQuests.size());

        for (int i = 0; i < count; i++) {

            allQuests
                    .get(i)
                    .setFeatured(true);
        }

        questRepository.saveAll(allQuests);

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
                        "New Quests Available",
                        "5 fresh challenges just dropped!"
                );
            }
        }
        System.out.println(
                "Quests rotated"
        );
    }
}