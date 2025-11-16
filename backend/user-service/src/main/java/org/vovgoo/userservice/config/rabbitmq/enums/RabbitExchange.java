package org.vovgoo.userservice.config.rabbitmq.enums;

import lombok.Getter;

@Getter
public enum RabbitExchange {
    USER_EVENTS("user-events");

    private final String name;

    RabbitExchange(String name) {
        this.name = name;
    }
}