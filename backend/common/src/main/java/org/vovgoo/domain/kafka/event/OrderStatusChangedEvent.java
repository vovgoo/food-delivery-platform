package org.vovgoo.domain.kafka.event;

import lombok.Builder;
import org.vovgoo.enums.order.OrderStatus;

import java.util.UUID;

@Builder
public record OrderStatusChangedEvent(
        UUID orderId,
        UUID userId,
        String userPhone,
        OrderStatus orderStatus
) {}
