package org.vovgoo.common.event.order.kafka;

import org.vovgoo.common.event.key.KafkaEventKey;
import org.vovgoo.common.event.order.kafka.event.OrderCreatedEvent;
import org.vovgoo.common.event.order.kafka.event.OrderStatusChangedEvent;

public class OrderKafkaEventKeys {

    private OrderKafkaEventKeys() {}

    public static final String ORDER_CREATED_TOPIC = "order.created";
    public static final String ORDER_STATUS_CHANGED_TOPIC = "order.status.changed";

    public static final KafkaEventKey<OrderCreatedEvent> ORDER_CREATED =
            new KafkaEventKey<>(ORDER_CREATED_TOPIC, OrderCreatedEvent.class);

    public static final KafkaEventKey<OrderStatusChangedEvent> ORDER_STATUS_CHANGED =
            new KafkaEventKey<>(ORDER_STATUS_CHANGED_TOPIC, OrderStatusChangedEvent.class);
}
