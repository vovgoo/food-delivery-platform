package org.vovgoo.orderservice.dto.order.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.vovgoo.orderservice.dto.orderItem.request.AddOrderItemRequest;
import org.vovgoo.orderservice.dto.payment.request.AddPaymentRequest;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request to create a new order")
public record CreateOrderRequest(

        @Schema(description = "Unique restaurant ID", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Идентификатор ресторана не может быть пустым")
        UUID restaurantId,

        @Schema(description = "Delivery address ID", example = "660e8400-e29b-41d4-a716-446655440111")
        @NotNull(message = "Идентификатор адреса не может быть пустым")
        UUID deliveryAddress,

        @Schema(description = "List of order items", example = "[{\"dishId\":\"770e8400-e29b-41d4-a716-446655440222\",\"quantity\":2}]")
        @NotEmpty(message = "Список блюд не может быть пустым")
        @Valid
        List<AddOrderItemRequest> items,

        @Schema(description = "Payment details")
        @Valid
        AddPaymentRequest payment
) {}