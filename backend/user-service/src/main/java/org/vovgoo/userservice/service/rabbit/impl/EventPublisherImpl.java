package org.vovgoo.userservice.service.rabbit.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbit.RabbitMQConfig;
import org.vovgoo.userservice.domain.rabbit.key.EventKey;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;

@Service
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private static final String EXCHANGE = RabbitMQConfig.EXCHANGE;

    @Retryable(
            retryFor = AmqpException.class,
            backoff = @Backoff(delay = 2000)
    )
    public <T> void publish(EventKey<T> eventKey, T payload) {
        if (!eventKey.payloadType().isAssignableFrom(payload.getClass())) {
            throw new IllegalArgumentException(
                    "Неверный тип payload для routing key " + eventKey.key()
            );
        }

        try {
            String json = objectMapper.writeValueAsString(payload);
            rabbitTemplate.convertAndSend(EXCHANGE, eventKey.key(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "Ошибка сериализации события для RabbitMQ", e
            );
        }
    }
}
