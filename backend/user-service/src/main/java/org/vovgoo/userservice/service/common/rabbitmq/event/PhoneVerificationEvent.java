package org.vovgoo.userservice.service.common.rabbitmq.event;

import lombok.Builder;

@Builder
public record PhoneVerificationEvent(
    String phone,
    String code
) {}