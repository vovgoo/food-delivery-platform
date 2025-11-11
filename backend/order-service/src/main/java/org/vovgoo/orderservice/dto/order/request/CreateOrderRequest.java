package org.vovgoo.orderservice.dto.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.vovgoo.orderservice.dto.orderItem.request.AddOrderItemRequest;
import org.vovgoo.orderservice.dto.payment.request.AddPaymentRequest;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "ID ресторана не может быть пустым")
        Long restaurantId,

        @NotEmpty(message = "Список блюд не может быть пустым")
        @Valid
        List<AddOrderItemRequest> items,

        @Valid
        AddPaymentRequest payment
) {}
