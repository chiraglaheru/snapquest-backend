package com.snapquest.snapquest.dto;

public record UserStatsResponse(

        int xp,

        int completed,

        int submitted,

        int wins,

        int losses,

        int successRate

) {}