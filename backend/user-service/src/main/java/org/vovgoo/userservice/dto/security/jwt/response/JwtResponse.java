package org.vovgoo.userservice.dto.security.jwt.response;

import lombok.Builder;

@Builder
public record JwtResponse(
        String accessToken
) {}
