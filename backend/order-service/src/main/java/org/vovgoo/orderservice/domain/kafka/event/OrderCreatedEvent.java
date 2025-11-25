package org.vovgoo.orderservice.domain.kafka.event;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderCreatedEvent(
        UUID orderId,
        UUID userId,
        String userPhone,
        LocalDateTime orderDate,
        BigDecimal totalPrice
) {}
