package org.vovgoo.orderservice.domain.kafka.key;

import org.vovgoo.orderservice.config.kafka.KafkaConfig;
import org.vovgoo.domain.kafka.event.OrderCreatedEvent;
import org.vovgoo.domain.kafka.event.OrderStatusChangedEvent;

public final class KafkaEvents {

    private KafkaEvents() {}

    public static final KafkaEvent<OrderCreatedEvent> ORDER_CREATED =
            new KafkaEvent<>(KafkaConfig.ORDER_CREATED_TOPIC, OrderCreatedEvent.class);

    public static final KafkaEvent<OrderStatusChangedEvent> ORDER_STATUS_CHANGED =
            new KafkaEvent<>(KafkaConfig.ORDER_STATUS_CHANGED_TOPIC, OrderStatusChangedEvent.class);
}
