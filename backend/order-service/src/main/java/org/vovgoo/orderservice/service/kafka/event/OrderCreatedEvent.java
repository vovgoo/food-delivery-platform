package org.vovgoo.orderservice.service.kafka.event;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderCreatedEvent(
        UUID orderId,
        String userPhone,
        LocalDateTime orderDate,
        BigDecimal totalPrice
) {}
