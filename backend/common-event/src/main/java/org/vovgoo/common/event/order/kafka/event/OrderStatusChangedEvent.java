package org.vovgoo.common.event.order.kafka.event;

import lombok.Builder;
import org.vovgoo.common.domain.order.enums.OrderStatus;

import java.util.UUID;

@Builder
public record OrderStatusChangedEvent(
        UUID orderId,
        UUID userId,
        String userPhone,
        OrderStatus orderStatus
) {}
