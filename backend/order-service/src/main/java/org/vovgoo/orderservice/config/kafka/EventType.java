package org.vovgoo.orderservice.config.kafka;

import lombok.Getter;
import org.vovgoo.orderservice.service.kafka.event.OrderCreatedEvent;
import org.vovgoo.orderservice.service.kafka.event.OrderStatusChangedEvent;

@Getter
public enum EventType {

    ORDER_CREATED("order.created", OrderCreatedEvent.class),
    ORDER_STATUS_CHANGED("order.status.changed", OrderStatusChangedEvent.class);

    private final String topic;
    private final Class<?> eventClass;

    EventType(String topic, Class<?> eventClass) {
        this.topic = topic;
        this.eventClass = eventClass;
    }
}