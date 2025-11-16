package org.vovgoo.userservice.config.rabbitmq.enums;

import lombok.Getter;

@Getter
public enum RabbitRoutingKey {
    PHONE_VERIFICATION_REQUESTED("phone-verification-requested");

    private final String name;

    RabbitRoutingKey(String name) {
        this.name = name;
    }
}
