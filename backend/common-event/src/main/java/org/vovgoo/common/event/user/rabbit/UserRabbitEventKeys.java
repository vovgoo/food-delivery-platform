package org.vovgoo.common.event.user.rabbit;

import org.vovgoo.common.event.key.RabbitEventKey;
import org.vovgoo.common.event.user.rabbit.event.EmailChangeEvent;
import org.vovgoo.common.event.user.rabbit.event.PhoneChangeEvent;
import org.vovgoo.common.event.user.rabbit.event.SignUpPhoneEvent;

import java.time.Duration;

public final class UserRabbitEventKeys {
    private UserRabbitEventKeys() {}

    public static final String SIGNUP_PHONE_EXCHANGE = "user.events";
    public static final String SIGNUP_PHONE_QUEUE = "signup.phone.queue";
    public static final String SIGNUP_PHONE_ROUTING_KEY = "signup.phone";

    public static final RabbitEventKey<SignUpPhoneEvent> SIGNUP_PHONE =
            new RabbitEventKey<>(
                    SIGNUP_PHONE_EXCHANGE,
                    SIGNUP_PHONE_QUEUE,
                    SIGNUP_PHONE_ROUTING_KEY,
                    SignUpPhoneEvent.class,
                    Duration.ofMinutes(10)
            );

    public static final String PHONE_CHANGE_EXCHANGE = "user.events";
    public static final String PHONE_CHANGE_QUEUE = "phone.change.queue";
    public static final String PHONE_CHANGE_ROUTING_KEY = "phone.change";

    public static final RabbitEventKey<PhoneChangeEvent> PHONE_CHANGE =
            new RabbitEventKey<>(
                    PHONE_CHANGE_EXCHANGE,
                    PHONE_CHANGE_QUEUE,
                    PHONE_CHANGE_ROUTING_KEY,
                    PhoneChangeEvent.class,
                    Duration.ofMinutes(10)
            );

    public static final String EMAIL_CHANGE_EXCHANGE = "user.events";
    public static final String EMAIL_CHANGE_QUEUE = "email.change.queue";
    public static final String EMAIL_CHANGE_ROUTING_KEY = "email.change";

    public static final RabbitEventKey<EmailChangeEvent> EMAIL_CHANGE =
            new RabbitEventKey<>(
                    EMAIL_CHANGE_EXCHANGE,
                    EMAIL_CHANGE_QUEUE,
                    EMAIL_CHANGE_ROUTING_KEY,
                    EmailChangeEvent.class,
                    Duration.ofMinutes(10)
            );
}
