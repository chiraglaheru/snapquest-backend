package com.snapquest.snapquest.service;

import com.snapquest.snapquest.model.Quest;
import com.snapquest.snapquest.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestService {

    private final QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public List<Quest> getAllActive() {

        List<Quest> quests =
                questRepository.findByFeaturedTrue();

        System.out.println(
                "QUEST COUNT = " + quests.size()
        );

        return quests;
    }

    public Quest getById(Long id) {
        return questRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quest not found: " + id));
    }

    public Quest create(String title, String description, String challenge, Integer points) {
        Quest quest = new Quest();
        quest.setTitle(title);
        quest.setDescription(description);
        quest.setChallenge(challenge);
        quest.setPoints(points);
        return questRepository.save(quest);
    }
}