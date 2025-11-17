package org.vovgoo.userservice.service.rabbit.event;

import lombok.Builder;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

@Builder
public record PhoneVerificationEvent(
    PhoneVerificationType phoneVerificationType,
    String phone,
    String code
) {}