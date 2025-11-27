package org.vovgoo.orderservice.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.common.event.key.KafkaEventKey;
import org.vovgoo.orderservice.exception.custom.kafka.KafkaEventTypeMismatchException;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Retryable(
            retryFor = KafkaException.class,
            backoff = @Backoff(delay = 2000)
    )
    public <T> void publish(KafkaEventKey<T> eventKey, T payload) {

        if (!eventKey.getPayloadType().isAssignableFrom(payload.getClass())) {
            throw new KafkaEventTypeMismatchException();
        }

        kafkaTemplate.send(eventKey.getTopic(), payload);
    }
}
