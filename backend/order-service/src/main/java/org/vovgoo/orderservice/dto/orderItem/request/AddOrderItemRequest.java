package org.vovgoo.orderservice.dto.orderItem.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddOrderItemRequest(
        @NotNull(message = "Идентификатор блюда обязателен")
        UUID dishId,

        @NotNull(message = "Количество обязано быть заполнено")
        @Min(value = 1, message = "Количество должно быть не меньше 1")
        Long quantity
) {}
