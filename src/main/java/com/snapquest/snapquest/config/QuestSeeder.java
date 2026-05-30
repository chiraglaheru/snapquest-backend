package com.snapquest.snapquest.config;

import com.snapquest.snapquest.model.Quest;
import com.snapquest.snapquest.repository.QuestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class QuestSeeder implements CommandLineRunner {

    private final QuestRepository questRepository;

    public QuestSeeder(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    @Override
    public void run(String... args) {

        if (questRepository.count() > 0) {
            return;
        }

        List<Quest> quests = List.of(

                // EASY (10 XP) — 5 featured so /api/quests returns data immediately on first boot
                new Quest("Find a sunset",        "Take a photo of a sunset",        10, true),
                new Quest("Find a shoe",           "Take a photo of a shoe",           10, true),
                new Quest("Find something unusual","Take a photo of something unusual",10, true),
                new Quest("Find a pen",            "Take a photo of a pen",            10, true),
                new Quest("Find a phone",          "Take a photo of a phone",          10, true),

                // EASY (10 XP) — remaining, not featured
                new Quest("Find a water bottle",   "Take a photo of a water bottle",   10, false),
                new Quest("Find a mug",            "Take a photo of a mug",            10, false),
                new Quest("Find a tree",           "Take a photo of a tree",           10, false),
                new Quest("Find a book",           "Take a photo of a book",           10, false),
                new Quest("Find a backpack",       "Take a photo of a backpack",       10, false),
                new Quest("Find a chair",          "Take a photo of a chair",          10, false),
                new Quest("Find a keyboard",       "Take a photo of a keyboard",       10, false),
                new Quest("Find headphones",       "Take a photo of headphones",       10, false),
                new Quest("Find a door",           "Take a photo of a door",           10, false),
                new Quest("Find a clock",          "Take a photo of a clock",          10, false),
                new Quest("Find a pillow",         "Take a photo of a pillow",         10, false),
                new Quest("Find a charger",        "Take a photo of a charger",        10, false),
                new Quest("Find glasses",          "Take a photo of glasses",          10, false),
                new Quest("Find a spoon",          "Take a photo of a spoon",          10, false),
                new Quest("Find a plant",          "Take a photo of a plant",          10, false),
                new Quest("Find a parked car",     "Take a photo of a parked car",     10, false),
                new Quest("Find a bicycle",        "Take a photo of a bicycle",        10, false),

                // MEDIUM (25 XP)
                new Quest("Find something red",              "Take a photo of something red",              25, false),
                new Quest("Find something blue",             "Take a photo of something blue",             25, false),
                new Quest("Find something yellow",           "Take a photo of something yellow",           25, false),
                new Quest("Find something circular",         "Take a photo of something circular",         25, false),
                new Quest("Find something metal",            "Take a photo of something metal",            25, false),
                new Quest("Find something wooden",           "Take a photo of something wooden",           25, false),
                new Quest("Find something reflective",       "Take a photo of something reflective",       25, false),
                new Quest("Find a flower",                   "Take a photo of a flower",                   25, false),
                new Quest("Find a streetlight",              "Take a photo of a streetlight",              25, false),
                new Quest("Find a traffic sign",             "Take a photo of a traffic sign",             25, false),
                new Quest("Find a playground",               "Take a photo of a playground",               25, false),
                new Quest("Find a bench",                    "Take a photo of a bench",                    25, false),
                new Quest("Find three people together",      "Take a photo showing three people together", 25, false),
                new Quest("Find someone wearing a hat",      "Take a photo of someone wearing a hat",      25, false),
                new Quest("Find someone carrying a bag",     "Take a photo of someone carrying a bag",     25, false),
                new Quest("Find something bigger than your head", "Take a photo of something bigger than your head", 25, false),
                new Quest("Find something tiny",             "Take a photo of something tiny",             25, false),
                new Quest("Find a tall building",            "Take a photo of a tall building",            25, false),
                new Quest("Find something with wheels",      "Take a photo of something with wheels",      25, false),
                new Quest("Find a road intersection",        "Take a photo of a road intersection",        25, false),

                // HARD (50 XP)
                new Quest("Find a dog",                               "Take a photo of a dog",                                        50, false),
                new Quest("Find a cat",                               "Take a photo of a cat",                                        50, false),
                new Quest("Find a rainbow",                           "Take a photo of a rainbow",                                    50, false),
                new Quest("Find a train",                             "Take a photo of a train",                                      50, false),
                new Quest("Find a fountain",                          "Take a photo of a fountain",                                   50, false),
                new Quest("Find a statue",                            "Take a photo of a statue",                                     50, false),
                new Quest("Find a police vehicle",                    "Take a photo of a police vehicle",                             50, false),
                new Quest("Find a horse",                             "Take a photo of a horse",                                      50, false),
                new Quest("Find something abandoned",                 "Take a photo of something abandoned",                          50, false),
                new Quest("Find the highest object nearby",           "Take a photo of the highest object nearby",                    50, false),
                new Quest("Find the oldest looking object nearby",    "Take a photo of the oldest looking object nearby",             50, false),
                new Quest("Find something never photographed before", "Take a creative photo of something never photographed before", 50, false),
                new Quest("Find the weirdest object nearby",          "Take a photo of the weirdest object nearby",                   50, false),
                new Quest("Find something that looks like a face",    "Take a photo of something that looks like a face",             50, false),
                new Quest("Find something that looks angry",          "Take a photo of something that looks angry",                   50, false),
                new Quest("Find a crowded place",                     "Take a photo of a crowded place",                              50, false),
                new Quest("Find an empty place",                      "Take a photo of an empty place",                               50, false),
                new Quest("Find something that does not belong",      "Take a photo of something that looks out of place",            50, false)
        );

        questRepository.saveAll(quests);

        System.out.println("seeded " + quests.size() + " quests ("
                + quests.stream().filter(Quest::getFeatured).count() + " featured)");
    }
}