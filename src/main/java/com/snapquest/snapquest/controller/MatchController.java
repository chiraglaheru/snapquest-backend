package com.snapquest.snapquest.controller;

import com.snapquest.snapquest.model.Match;
import com.snapquest.snapquest.model.User;
import com.snapquest.snapquest.repository.MatchRepository;
import com.snapquest.snapquest.repository.UserRepository;
import com.snapquest.snapquest.service.MatchService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    private final UserRepository userRepository;

    private final MatchRepository matchRepository;

    public MatchController(
            MatchService matchService,
            UserRepository userRepository,
            MatchRepository matchRepository
    ) {

        this.matchService =
                matchService;

        this.userRepository =
                userRepository;

        this.matchRepository =
                matchRepository;
    }

    @PostMapping("/find")
    public Match findMatch(
            @RequestBody MatchRequest request,
            Authentication authentication
    ) {

        String username =
                authentication.getName();

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow();

        return matchService.findMatch(
                user.getId(),
                request.latitude(),
                request.longitude()
        );
    }

    @GetMapping("/my")
    public Match getMyMatch(
            Authentication authentication
    ) {

        String username =
                authentication.getName();

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow();

        return matchService.getMyActiveMatch(
                user.getId()
        );
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitMatch(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String username =
                authentication.getName();

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow();

        Match match =
                matchRepository
                        .findById(id)
                        .orElseThrow();

        if (
                match.getStatus()
                        .equals("FINISHED")
        ) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Match already finished"
                    );
        }

        Long userId =
                user.getId();

        if (
                match.getPlayer1Id()
                        .equals(userId)
        ) {

            if (
                    match.getPlayer1Submitted()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Already submitted"
                        );
            }

            match.setPlayer1Submitted(
                    true
            );

        } else if (
                match.getPlayer2Id()
                        .equals(userId)
        ) {

            if (
                    match.getPlayer2Submitted()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Already submitted"
                        );
            }

            match.setPlayer2Submitted(
                    true
            );
        }

        if (match.getWinnerId() == null) {

            match.setWinnerId(userId);

            match.setStatus("FINISHED");

            System.out.println(
                    "WINNER SAVED: "
                            + userId
            );

            matchRepository.save(match);

            User winner =
                    userRepository
                            .findById(userId)
                            .orElseThrow();

            winner.setXp(
                    winner.getXp() + 50
            );

            userRepository.save(winner);
        }

        matchRepository.save(match);

        return ResponseEntity.ok(
                match
        );
    }

    record MatchRequest(
            Double latitude,
            Double longitude
    ) {}
}