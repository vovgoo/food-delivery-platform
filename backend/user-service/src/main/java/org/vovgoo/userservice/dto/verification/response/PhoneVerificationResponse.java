package org.vovgoo.userservice.dto.verification.response;

import lombok.Builder;

@Builder
public record PhoneVerificationResponse(
    String token
) { }
