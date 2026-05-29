package com.snapquest.snapquest.controller;

import com.snapquest.snapquest.dto.LeaderboardUserResponse;
import com.snapquest.snapquest.dto.UserStatsResponse;
import com.snapquest.snapquest.model.User;
import com.snapquest.snapquest.repository.MatchRepository;
import com.snapquest.snapquest.repository.SubmissionRepository;
import com.snapquest.snapquest.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final MatchRepository matchRepository;

    public UserController(
            UserRepository userRepository,
            SubmissionRepository submissionRepository,
            MatchRepository matchRepository
    ) {

        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/me")
    public User getMe(
            Authentication authentication
    ) {

        String username =
                authentication.getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow();
    }

    @GetMapping("/stats")
    public UserStatsResponse getStats(
            Authentication authentication
    ) {

        String username =
                authentication.getName();

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow();

        int xp =
                user.getXp();

        int completed =
                submissionRepository
                        .countByUserIdAndVerifiedTrue(
                                user.getId()
                        );

        int submitted =
                submissionRepository
                        .countByUserId(
                                user.getId()
                        );

        int wins =
                matchRepository
                        .countByWinnerId(
                                user.getId()
                        );

        int losses =
                Math.max(
                        0,
                        submitted - wins
                );

        int successRate =
                submitted == 0
                        ? 0
                        : (completed * 100)
                          / submitted;

        return new UserStatsResponse(
                xp,
                completed,
                submitted,
                wins,
                losses,
                successRate
        );
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardUserResponse> getLeaderboard() {

        return userRepository
                .findTop50ByOrderByXpDesc()
                .stream()
                .map(user -> {

                    int xp = user.getXp();

                    String tier;

                    if (xp >= 1000) {
                        tier = "Legend";
                    } else if (xp >= 600) {
                        tier = "Ranger";
                    } else if (xp >= 300) {
                        tier = "Scout";
                    } else if (xp >= 100) {
                        tier = "Hunter";
                    } else {
                        tier = "Rookie";
                    }

                    int wins =
                            matchRepository
                                    .countByWinnerId(
                                            user.getId()
                                    );

                    return new LeaderboardUserResponse(
                            user.getUsername(),
                            xp,
                            wins,
                            tier
                    );
                })
                .toList();
    }
}