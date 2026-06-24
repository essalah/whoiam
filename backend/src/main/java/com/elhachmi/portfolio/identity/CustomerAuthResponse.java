package com.elhachmi.portfolio.identity;

import java.util.UUID;

public record CustomerAuthResponse(
        String token,
        UUID userId,
        String email,
        String displayName,
        long expiresIn
) {
}
