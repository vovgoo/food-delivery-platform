package org.vovgoo.orderservice.dto.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.vovgoo.orderservice.dto.orderItem.request.AddOrderItemRequest;
import org.vovgoo.orderservice.dto.payment.request.AddPaymentRequest;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull(message = "Идентификатор ресторана не может быть пустым")
        UUID restaurantId,

        @NotNull(message = "Идентификатор адреса не может быть пустым")
        UUID deliveryAddress,

        @NotEmpty(message = "Список блюд не может быть пустым")
        @Valid
        List<AddOrderItemRequest> items,

        @Valid
        AddPaymentRequest payment
) {}
