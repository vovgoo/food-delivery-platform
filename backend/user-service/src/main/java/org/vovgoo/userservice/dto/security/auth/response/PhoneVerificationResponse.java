package org.vovgoo.userservice.dto.security.auth.response;

import lombok.Builder;

@Builder
public record PhoneVerificationResponse(
    String token
) { }
