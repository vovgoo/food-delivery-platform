package org.vovgoo.orderservice.service.kafka.event;

import lombok.Builder;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

import java.util.UUID;

@Builder
public record OrderStatusChangedEvent(
        UUID orderId,
        String userPhone,
        OrderStatus orderStatus
) {}
