package com.elhachmi.portfolio.dto.response;

public record AuthResponse(
        String token,
        String username,
        long expiresIn
) {}
