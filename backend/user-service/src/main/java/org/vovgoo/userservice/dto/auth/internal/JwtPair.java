package org.vovgoo.userservice.dto.auth.internal;

public record JwtPair(
        String accessToken,
        String refreshToken
) {}
