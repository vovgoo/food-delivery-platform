package org.vovgoo.orderservice.dto.order.response;

import org.vovgoo.orderservice.dto.payment.response.PaymentResponse;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderShortResponse(
        UUID id,
        OrderStatus status,
        LocalDateTime orderDate,
        BigDecimal totalPrice,
        PaymentResponse payment
) {}
