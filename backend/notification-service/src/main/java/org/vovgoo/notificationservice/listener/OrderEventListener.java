package org.vovgoo.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.vovgoo.common.event.order.kafka.OrderKafkaEventKeys;
import org.vovgoo.common.event.order.kafka.event.OrderCreatedEvent;
import org.vovgoo.common.event.order.kafka.event.OrderStatusChangedEvent;
import org.vovgoo.notificationservice.service.sender.sms.SmsSender;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final SmsSender smsSender;

    @KafkaListener(topics = OrderKafkaEventKeys.ORDER_CREATED_TOPIC, groupId = "notification-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        smsSender.send(event.userPhone(), String.format("Ваш заказ успешно принят, его номер: %s", event.orderId()));
    }

    @KafkaListener(topics = OrderKafkaEventKeys.ORDER_STATUS_CHANGED_TOPIC, groupId = "notification-group")
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        smsSender.send(event.userPhone(), String.format("Статус вашего заказа с номером %s был изменен на %s", event.orderId(), event.orderStatus()));
    }
}
