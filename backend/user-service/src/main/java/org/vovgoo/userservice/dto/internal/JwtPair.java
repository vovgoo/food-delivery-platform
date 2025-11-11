package org.vovgoo.userservice.dto.internal;

public record JwtPair(
        String accessToken,
        String refreshToken
) {}
