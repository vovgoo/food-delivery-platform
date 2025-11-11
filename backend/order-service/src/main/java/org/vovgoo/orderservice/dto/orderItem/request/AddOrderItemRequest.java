package org.vovgoo.orderservice.dto.orderItem.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddOrderItemRequest(
        @NotNull(message = "ID блюда обязателен")
        Long dishId,

        @NotNull(message = "Количество обязано быть заполнено")
        @Min(value = 1, message = "Количество должно быть не меньше 1")
        Long quantity,

        @NotNull(message = "Цена обязательна")
        @DecimalMin(value = "0.01", message = "Цена должна быть положительной")
        @Digits(integer = 8, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
        BigDecimal price
) {}
