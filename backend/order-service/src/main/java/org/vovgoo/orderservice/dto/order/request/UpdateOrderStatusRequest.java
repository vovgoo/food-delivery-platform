package org.vovgoo.orderservice.dto.order.request;

import jakarta.validation.constraints.NotNull;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

public record UpdateOrderStatusRequest(
        @NotNull(message = "Статус заказа не может быть пустым")
        OrderStatus status
) { }
