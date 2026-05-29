package com.snapquest.snapquest.dto;

public record LeaderboardUserResponse(

        String username,
        int xp,
        int wins,
        String tier

) {}