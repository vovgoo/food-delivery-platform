package org.vovgoo.orderservice.dto.order.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.vovgoo.enums.order.OrderStatus;

@Schema(description = "Request to update the status of an order")
public record UpdateOrderStatusRequest(

        @Schema(description = "New status of the order", example = "CONFIRMED")
        @NotNull(message = "Статус заказа не может быть пустым")
        OrderStatus status
) {}
