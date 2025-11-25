package org.vovgoo.orderservice.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.orderservice.domain.kafka.key.KafkaEvent;
import org.vovgoo.orderservice.exception.custom.kafka.KafkaEventTypeMismatchException;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Retryable(
            retryFor = KafkaException.class,
            backoff = @Backoff(delay = 2000)
    )
    public <T> void publish(KafkaEvent<T> eventKey, T payload) {

        if (!eventKey.payloadType().isAssignableFrom(payload.getClass())) {
            throw new KafkaEventTypeMismatchException();
        }

        kafkaTemplate.send(eventKey.topic(), payload);
    }
}
