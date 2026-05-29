package com.snapquest.snapquest.controller;

import com.snapquest.snapquest.model.Quest;
import com.snapquest.snapquest.service.QuestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quests")
public class QuestController {

    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping
    public ResponseEntity<List<Quest>> getAllActive() {
        return ResponseEntity.ok(questService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(questService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Quest> create(@Valid @RequestBody CreateQuestRequest request) {
        Quest quest = questService.create(
                request.title(),
                request.description(),
                request.challenge(),
                request.points());
        return ResponseEntity.ok(quest);
    }

    record CreateQuestRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotBlank String challenge,
            @Positive Integer points) {}
}