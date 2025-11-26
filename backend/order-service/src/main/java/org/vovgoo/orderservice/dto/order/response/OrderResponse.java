package org.vovgoo.orderservice.dto.order.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.orderservice.dto.address.response.AddressResponse;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.dto.payment.response.PaymentResponse;
import org.vovgoo.orderservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.orderservice.dto.user.response.UserResponse;
import org.vovgoo.enums.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detailed response for an order")
public record OrderResponse(

        @Schema(description = "Unique order ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "User who placed the order")
        UserResponse user,

        @Schema(description = "Current status of the order", example = "CONFIRMED")
        OrderStatus status,

        @Schema(description = "Date and time when the order was placed", example = "2025-11-22T15:30:00")
        LocalDateTime orderDate,

        @Schema(description = "Delivery address of the order")
        AddressResponse address,

        @Schema(description = "Restaurant from which the order was placed")
        RestaurantResponse restaurant,

        @Schema(description = "Total price of the order", example = "123.45")
        BigDecimal totalPrice,

        @Schema(description = "List of ordered items")
        List<OrderItemResponse> items,

        @Schema(description = "Payment details for the order")
        PaymentResponse payment
) {}
