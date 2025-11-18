package org.vovgoo.userservice.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitQueue;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.rabbitmq.property.RabbitProperty;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperty rabbitProperty;

    @Bean
    public DirectExchange userEventsExchange() {
        String exchangeName = rabbitProperty.getExchangeName(RabbitExchange.USER_EVENTS);
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue phoneVerificationQueue() {
        String queueName = rabbitProperty.getQueueName(RabbitQueue.PHONE_VERIFICATION);
        return new Queue(queueName, true);
    }

    @Bean
    public Queue emailVerificationQueue() {
        String queueName = rabbitProperty.getQueueName(RabbitQueue.EMAIL_VERIFICATION);
        return new Queue(queueName, true);
    }

    @Bean
    public Binding phoneVerificationBinding(Queue phoneVerificationQueue, DirectExchange userEventsExchange) {
        return BindingBuilder.bind(phoneVerificationQueue)
                .to(userEventsExchange)
                .with(RabbitRoutingKey.PHONE_VERIFICATION_REQUESTED.getName());
    }

    @Bean
    public Binding emailVerificationBinding(Queue emailVerificationQueue, DirectExchange userEventsExchange) {
        return BindingBuilder.bind(emailVerificationQueue)
                .to(userEventsExchange)
                .with(RabbitRoutingKey.EMAIL_VERIFICATION_REQUESTED.getName());
    }
}
