package org.vovgoo.restaurantservice.dto.dish.request;

import jakarta.validation.constraints.*;
import org.vovgoo.restaurantservice.entity.enums.DishStatus;
import org.vovgoo.restaurantservice.validation.dish.status.AllowedDishStatus;

import java.math.BigDecimal;

public record DishUpdateRequest(

        @NotBlank(message = "Название блюда не может быть пустым")
        @Size(min = 2, max = 100, message = "Название блюда должно быть от 2 до 100 символов")
        String name,

        @Size(max = 500, message = "Описание блюда должно быть до 500 символов")
        String description,

        @PositiveOrZero(message = "Вес блюда должен быть положительным или нулевым")
        Integer portionInGrams,

        @PositiveOrZero(message = "Количество белков должно быть положительным или нулевым")
        Double proteins,

        @PositiveOrZero(message = "Количество жиров должно быть положительным или нулевым")
        Double fats,

        @PositiveOrZero(message = "Количество углеводов должно быть положительным или нулевым")
        Double carbohydrates,

        @NotNull(message = "Необходимо указать, острое ли блюдо")
        Boolean spicy,

        @NotNull(message = "Необходимо указать, подходит ли блюдо для веганов")
        Boolean vegan,

        @NotNull(message = "Необходимо указать, подходит ли блюдо для вегетарианцев")
        Boolean vegetarian,

        @NotNull(message = "Цена блюда обязательна")
        @DecimalMin(value = "0.01", message = "Цена блюда должна быть больше 0")
        @Digits(integer = 6, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
        BigDecimal price,

        @NotNull(message = "Статус блюда не может быть пустым")
        @AllowedDishStatus(anyOf = {DishStatus.AVAILABLE, DishStatus.TEMPORARY_UNAVAILABLE},
                message = "Статус блюда должен быть AVAILABLE или TEMPORARY_UNAVAILABLE")
        DishStatus status
) {}
