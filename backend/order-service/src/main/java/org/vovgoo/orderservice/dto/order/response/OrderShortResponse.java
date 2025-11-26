package org.vovgoo.orderservice.dto.order.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.orderservice.dto.payment.response.PaymentResponse;
import org.vovgoo.enums.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Short response with order summary details")
public record OrderShortResponse(

        @Schema(description = "Unique order ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Current status of the order", example = "CONFIRMED")
        OrderStatus status,

        @Schema(description = "Date and time when the order was placed", example = "2025-11-22T15:30:00")
        LocalDateTime orderDate,

        @Schema(description = "Total price of the order", example = "123.45")
        BigDecimal totalPrice,

        @Schema(description = "Payment details for the order")
        PaymentResponse payment
) {}
