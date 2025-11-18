package org.vovgoo.userservice.config.rabbitmq.enums;

import lombok.Getter;

@Getter
public enum RabbitQueue {
    PHONE_VERIFICATION("phone-verification"),
    EMAIL_VERIFICATION("email-verification");

    private final String name;

    RabbitQueue(String name) {
        this.name = name;
    }
}