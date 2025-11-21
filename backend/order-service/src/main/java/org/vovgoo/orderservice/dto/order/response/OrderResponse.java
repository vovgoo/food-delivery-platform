package org.vovgoo.orderservice.dto.order.response;

import org.vovgoo.orderservice.dto.address.response.AddressResponse;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.dto.payment.response.PaymentResponse;
import org.vovgoo.orderservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.orderservice.dto.user.response.UserResponse;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UserResponse user,
        OrderStatus status,
        LocalDateTime orderDate,
        AddressResponse address,
        RestaurantResponse restaurant,
        BigDecimal totalPrice,
        List<OrderItemResponse> items,
        PaymentResponse payment
) {}
