package org.vovgoo.userservice.dto.security.jwt.internal;

import lombok.Builder;

@Builder
public record JwtPair(
        String accessToken,
        String refreshToken
) {}
