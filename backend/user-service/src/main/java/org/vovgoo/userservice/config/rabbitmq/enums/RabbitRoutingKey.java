package org.vovgoo.userservice.config.rabbitmq.enums;

import lombok.Getter;
import org.vovgoo.userservice.service.rabbit.event.EmailVerificationEvent;
import org.vovgoo.userservice.service.rabbit.event.PhoneVerificationEvent;

@Getter
public enum RabbitRoutingKey {
    PHONE_VERIFICATION_REQUESTED("phone-verification-requested", PhoneVerificationEvent.class),
    EMAIL_VERIFICATION_REQUESTED("email-verification-requested", EmailVerificationEvent.class);

    private final String name;
    private final Class<?> eventType;

    RabbitRoutingKey(String name, Class<?> eventType) {
        this.name = name;
        this.eventType = eventType;
    }
}
