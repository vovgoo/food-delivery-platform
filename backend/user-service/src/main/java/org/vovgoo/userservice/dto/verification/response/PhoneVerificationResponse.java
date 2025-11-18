package org.vovgoo.userservice.dto.verification.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response containing phone verification token")
public record PhoneVerificationResponse(

        @Schema(description = "Token used to verify the user's phone number", example = "550e8400-e29b-41d4-a716-446655440000")
        String token
) {}
