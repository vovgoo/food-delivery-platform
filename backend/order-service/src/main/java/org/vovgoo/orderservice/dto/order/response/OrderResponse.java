package org.vovgoo.orderservice.dto.order.response;

import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.dto.payment.response.PaymentResponse;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        LocalDateTime orderDate,
        Long restaurantId,
        BigDecimal totalPrice,
        List<OrderItemResponse> items,
        PaymentResponse payment
) {}
