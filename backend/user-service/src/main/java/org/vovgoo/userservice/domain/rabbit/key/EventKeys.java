package org.vovgoo.userservice.domain.rabbit.key;

import org.vovgoo.userservice.config.rabbit.RabbitMQConfig;
import org.vovgoo.userservice.domain.rabbit.event.EmailChangeEvent;
import org.vovgoo.userservice.domain.rabbit.event.PhoneChangeEvent;
import org.vovgoo.userservice.domain.rabbit.event.SignUpPhoneEvent;

import java.time.Duration;

public final class EventKeys {
    private EventKeys() {}

    public static final EventKey<SignUpPhoneEvent> SIGNUP_PHONE =
            new EventKey<>(RabbitMQConfig.SIGNUP_PHONE_KEY, SignUpPhoneEvent.class, Duration.ofMinutes(10));

    public static final EventKey<PhoneChangeEvent> PHONE_CHANGE =
            new EventKey<>(RabbitMQConfig.PHONE_CHANGE_KEY, PhoneChangeEvent.class, Duration.ofMinutes(10));

    public static final EventKey<EmailChangeEvent> EMAIL_CHANGE =
            new EventKey<>(RabbitMQConfig.EMAIL_CHANGE_KEY, EmailChangeEvent.class, Duration.ofMinutes(10));
}
