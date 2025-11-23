package org.vovgoo.userservice.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "user.events";

    public static final String SIGNUP_PHONE_KEY = "signup.phone";
    public static final String PHONE_CHANGE_KEY = "phone.change";
    public static final String EMAIL_CHANGE_KEY = "email.change";

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue signupPhoneQueue() {
        return new Queue("signup.phone.queue", true);
    }

    @Bean
    public Queue phoneChangeQueue() {
        return new Queue("phone.change.queue", true);
    }

    @Bean
    public Queue emailChangeQueue() {
        return new Queue("email.change.queue", true);
    }

    @Bean
    public Binding signupPhoneBinding(Queue signupPhoneQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(signupPhoneQueue)
                .to(userEventsExchange)
                .with(SIGNUP_PHONE_KEY);
    }

    @Bean
    public Binding phoneChangeBinding(Queue phoneChangeQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(phoneChangeQueue)
                .to(userEventsExchange)
                .with(PHONE_CHANGE_KEY);
    }

    @Bean
    public Binding emailChangeBinding(Queue emailChangeQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(emailChangeQueue)
                .to(userEventsExchange)
                .with(EMAIL_CHANGE_KEY);
    }
}
