package com.snapquest.snapquest.service;

import com.snapquest.snapquest.model.Match;
import com.snapquest.snapquest.model.Quest;
import com.snapquest.snapquest.repository.MatchRepository;
import com.snapquest.snapquest.repository.QuestRepository;
import org.springframework.stereotype.Service;
import com.snapquest.snapquest.repository.UserRepository;
import com.snapquest.snapquest.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public MatchService(
            MatchRepository matchRepository,
            QuestRepository questRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.matchRepository = matchRepository;
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Match getMyActiveMatch(Long userId) {

        return matchRepository
                .findTopByPlayer1IdOrPlayer2IdOrderByIdDesc(
                        userId,
                        userId
                )
                .orElse(null);
    }

    public Match findMatch(
            Long userId,
            Double latitude,
            Double longitude
    ) {

        List<Match> waitingMatches =
                matchRepository.findByStatus(
                        "WAITING"
                );

        for (Match match : waitingMatches) {

            if (
                    !"WAITING".equals(
                            match.getStatus()
                    )
            ) {
                continue;
            }

            if (
                    match.getPlayer1Id()
                            .equals(userId)
            ) {
                return match;
            }

            if (
                    match.getLatitude() == null
                            ||
                            match.getLongitude() == null
            ) {
                continue;
            }

            double distance =
                    distanceKm(
                            latitude,
                            longitude,
                            match.getLatitude(),
                            match.getLongitude()
                    );

            System.out.println(
                    "Distance: " + distance
            );

            if (distance <= 5) {

                match.setPlayer2Id(userId);
                match.setStatus("ACTIVE");

                Match savedMatch =
                        matchRepository.save(match);

                User player1 =
                        userRepository.findById(
                                match.getPlayer1Id()
                        ).orElse(null);

                User player2 =
                        userRepository.findById(
                                userId
                        ).orElse(null);

                if (
                        player1 != null &&
                                player1.getExpoPushToken() != null
                ) {

                    notificationService.sendPush(
                            player1.getExpoPushToken(),
                            "Opponent Found",
                            "A hunter is waiting for you."
                    );
                }

                if (
                        player2 != null &&
                                player2.getExpoPushToken() != null
                ) {

                    notificationService.sendPush(
                            player2.getExpoPushToken(),
                            "Opponent Found",
                            "A hunter is waiting for you."
                    );
                }

                return savedMatch;
            }
        }


        Match newMatch = new Match();
        newMatch.setLatitude(
                latitude
        );

        newMatch.setLongitude(
                longitude
        );

        newMatch.setPlayer1Id(userId);
        newMatch.setStatus("WAITING");

        List<Quest> quests = questRepository.findAll();

        if (!quests.isEmpty()) {

            Quest randomQuest =
                    quests.get(
                            new Random().nextInt(quests.size())
                    );

            newMatch.setQuestId(randomQuest.getId());
        }

        return matchRepository.save(newMatch);
    }

    private double distanceKm(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        final int R = 6371;

        double latDistance =
                Math.toRadians(
                        lat2 - lat1
                );

        double lonDistance =
                Math.toRadians(
                        lon2 - lon1
                );

        double a =
                Math.sin(latDistance / 2)
                        * Math.sin(latDistance / 2)
                        +
                        Math.cos(Math.toRadians(lat1))
                                * Math.cos(Math.toRadians(lat2))
                                *
                                Math.sin(lonDistance / 2)
                                * Math.sin(lonDistance / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return R * c;
    }
}