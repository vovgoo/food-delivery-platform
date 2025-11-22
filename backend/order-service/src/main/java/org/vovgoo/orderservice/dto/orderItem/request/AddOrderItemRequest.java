package org.vovgoo.orderservice.dto.orderItem.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request to add an item to an order")
public record AddOrderItemRequest(

        @Schema(description = "Unique ID of the dish", example = "770e8400-e29b-41d4-a716-446655440222")
        @NotNull(message = "Идентификатор блюда обязателен")
        UUID dishId,

        @Schema(description = "Quantity of the dish", example = "2")
        @NotNull(message = "Количество обязано быть заполнено")
        @Min(value = 1, message = "Количество должно быть не меньше 1")
        Long quantity
) {}
