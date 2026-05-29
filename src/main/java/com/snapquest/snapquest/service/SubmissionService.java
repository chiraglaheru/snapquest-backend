package com.snapquest.snapquest.service;

import com.snapquest.snapquest.model.Quest;
import com.snapquest.snapquest.model.Submission;
import com.snapquest.snapquest.model.User;
import com.snapquest.snapquest.repository.SubmissionRepository;
import com.snapquest.snapquest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final QuestService questService;
    private final VisionService visionService;
    private final NotificationService notificationService;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            UserRepository userRepository,
            QuestService questService,
            VisionService visionService,
            NotificationService notificationService
    ) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.questService = questService;
        this.visionService = visionService;
        this.notificationService = notificationService;
    }

    public Submission submit(
            String username,
            Long questId,
            String imageBase64
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + username
                        )
                );

        Quest quest = questService.getById(questId);

        if (
                submissionRepository.existsByUserIdAndQuestId(
                        user.getId(),
                        questId
                )
        ) {
            throw new RuntimeException(
                    "You already completed this quest"
            );
        }

        boolean verified =
                visionService.verifyImage(
                        imageBase64,
                        quest.getChallenge()
                );

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setQuest(quest);
        submission.setImageBase64(imageBase64);
        submission.setVerified(verified);

        submission.setVerificationNote(
                verified
                        ? "Image matches the challenge"
                        : "Image does not match the challenge"
        );

        if (verified) {

            System.out.println(
                    "XP AWARDED TO: "
                            + user.getUsername()
            );

            String oldTier =
                    getTier(user.getXp());

            user.setXp(
                    user.getXp()
                            + quest.getPoints()
            );

            String newTier =
                    getTier(user.getXp());

            userRepository.save(user);
            userRepository.flush();

            if (
                    !oldTier.equals(newTier)
                            &&
                            user.getExpoPushToken() != null
            ) {

                notificationService.sendPush(
                        user.getExpoPushToken(),
                        "Rank Up!",
                        "You are now a " + newTier
                );
            }

            User updatedUser =
                    userRepository
                            .findById(user.getId())
                            .orElseThrow();

            System.out.println(
                    "DB XP NOW: "
                            + updatedUser.getXp()
            );
        }

        return submissionRepository.save(submission);
    }

    public List<Submission> getByUser(
            String username
    ) {

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found: "
                                                + username
                                )
                        );

        return submissionRepository.findByUserId(
                user.getId()
        );
    }

    public List<Submission> getByQuest(
            Long questId
    ) {

        return submissionRepository.findByQuestId(
                questId
        );
    }

    private String getTier(int xp) {

        if (xp >= 1000) {
            return "Legend";
        } else if (xp >= 600) {
            return "Ranger";
        } else if (xp >= 300) {
            return "Scout";
        } else if (xp >= 100) {
            return "Hunter";
        }

        return "Rookie";
    }

}
