package org.vovgoo.orderservice.domain.kafka.key;

import org.vovgoo.orderservice.domain.kafka.event.OrderCreatedEvent;
import org.vovgoo.orderservice.domain.kafka.event.OrderStatusChangedEvent;

public final class KafkaEvents {

    private KafkaEvents() {}

    public static final KafkaEvent<OrderCreatedEvent> ORDER_CREATED =
            new KafkaEvent<>("order.created", OrderCreatedEvent.class);

    public static final KafkaEvent<OrderStatusChangedEvent> ORDER_STATUS_CHANGED =
            new KafkaEvent<>("order.status.changed", OrderStatusChangedEvent.class);
}
