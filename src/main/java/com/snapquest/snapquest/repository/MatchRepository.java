package com.snapquest.snapquest.repository;

import com.snapquest.snapquest.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository
        extends JpaRepository<Match, Long> {

    Optional<Match>
    findTopByPlayer1IdOrPlayer2IdOrderByIdDesc(
            Long player1Id,
            Long player2Id
    );

    List<Match> findByStatus(
            String status
    );

    int countByWinnerId(
            Long winnerId
    );
}