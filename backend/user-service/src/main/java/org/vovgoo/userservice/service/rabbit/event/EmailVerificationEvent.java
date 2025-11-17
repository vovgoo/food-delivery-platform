package org.vovgoo.userservice.service.rabbit.event;

import lombok.Builder;
import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;

@Builder
public record EmailVerificationEvent(
    EmailVerificationType emailVerificationType,
    String email,
    String token
) {}
