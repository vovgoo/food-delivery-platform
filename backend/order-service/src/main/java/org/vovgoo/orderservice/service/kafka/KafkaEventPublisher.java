package org.vovgoo.orderservice.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.orderservice.config.kafka.EventType;
import org.vovgoo.orderservice.exception.custom.kafka.InvalidEventTypeException;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T> void publish(EventType eventType, T event) {
        if (!eventType.getEventClass().isAssignableFrom(event.getClass())) {
            throw new InvalidEventTypeException();
        }

        kafkaTemplate.send(eventType.getTopic(), event);
    }
}
