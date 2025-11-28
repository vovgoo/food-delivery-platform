package org.vovgoo.userservice.service.rabbit.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.common.event.key.RabbitEventKey;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventTypeMismatchException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;

@Service
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Retryable(
            retryFor = AmqpException.class,
            backoff = @Backoff(delay = 2000)
    )
    public <T> void publish(RabbitEventKey<T> eventKey, T payload) {
        if (!eventKey.getPayloadType().isAssignableFrom(payload.getClass())) {
            throw new RabbitEventTypeMismatchException("Payload type does not match expected event key type " + eventKey.getRoutingKey());
        }

        rabbitTemplate.convertAndSend(eventKey.getExchange(), eventKey.getRoutingKey(), payload);
    }
}
