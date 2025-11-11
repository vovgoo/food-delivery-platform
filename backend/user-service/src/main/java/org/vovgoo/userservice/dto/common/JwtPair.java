package org.vovgoo.userservice.dto.common;

public record JwtPair(
        String accessToken,
        String refreshToken
) {}
