package org.vovgoo.orderservice.service.kafka.event;

import lombok.Builder;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

import java.util.UUID;

@Builder
public record OrderStatusChangedEvent(
        UUID orderId,
        UUID userId,
        String userPhone,
        OrderStatus orderStatus
) {}
