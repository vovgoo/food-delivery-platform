package org.vovgoo.userservice.service.rabbit.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbit.RabbitMQConfig;
import org.vovgoo.userservice.domain.rabbit.key.EventKey;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventTypeMismatchException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;

@Service
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = RabbitMQConfig.EXCHANGE;

    @Retryable(
            retryFor = AmqpException.class,
            backoff = @Backoff(delay = 2000)
    )
    public <T> void publish(EventKey<T> eventKey, T payload) {
        if (!eventKey.payloadType().isAssignableFrom(payload.getClass())) {
            throw new RabbitEventTypeMismatchException("Payload type does not match expected event key type " + eventKey.key());
        }

        rabbitTemplate.convertAndSend(EXCHANGE, eventKey.key(), payload);
    }
}
