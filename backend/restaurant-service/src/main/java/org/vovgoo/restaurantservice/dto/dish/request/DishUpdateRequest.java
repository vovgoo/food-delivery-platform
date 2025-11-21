package org.vovgoo.restaurantservice.dto.dish.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.vovgoo.enums.dish.DishStatus;
import org.vovgoo.restaurantservice.validation.dish.status.AllowedDishStatus;
import java.math.BigDecimal;

@Schema(description = "Request for updating an existing dish")
public record DishUpdateRequest(

        @Schema(description = "Dish name", example = "Маргарита")
        @NotBlank(message = "Название блюда не может быть пустым")
        @Size(min = 2, max = 100, message = "Название блюда должно быть от 2 до 100 символов")
        String name,

        @Schema(description = "Dish description", example = "Классическая пицца с томатным соусом и сыром")
        @Size(max = 500, message = "Описание блюда должно быть до 500 символов")
        String description,

        @Schema(description = "Portion size in grams", example = "350")
        @PositiveOrZero(message = "Вес блюда должен быть положительным или нулевым")
        Integer portionInGrams,

        @Schema(description = "Proteins content in grams", example = "12.5")
        @PositiveOrZero(message = "Количество белков должно быть положительным или нулевым")
        Double proteins,

        @Schema(description = "Fats content in grams", example = "10.0")
        @PositiveOrZero(message = "Количество жиров должно быть положительным или нулевым")
        Double fats,

        @Schema(description = "Carbohydrates content in grams", example = "45.0")
        @PositiveOrZero(message = "Количество углеводов должно быть положительным или нулевым")
        Double carbohydrates,

        @Schema(description = "Is the dish spicy?", example = "false")
        @NotNull(message = "Необходимо указать, острое ли блюдо")
        Boolean spicy,

        @Schema(description = "Is the dish suitable for vegans?", example = "false")
        @NotNull(message = "Необходимо указать, подходит ли блюдо для веганов")
        Boolean vegan,

        @Schema(description = "Is the dish suitable for vegetarians?", example = "true")
        @NotNull(message = "Необходимо указать, подходит ли блюдо для вегетарианцев")
        Boolean vegetarian,

        @Schema(description = "Dish price", example = "450.50")
        @NotNull(message = "Цена блюда обязательна")
        @DecimalMin(value = "0.01", message = "Цена блюда должна быть больше 0")
        @Digits(integer = 6, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
        BigDecimal price,

        @Schema(description = "Dish status", example = "AVAILABLE")
        @NotNull(message = "Статус блюда не может быть пустым")
        @AllowedDishStatus(anyOf = {DishStatus.AVAILABLE, DishStatus.TEMPORARY_UNAVAILABLE},
                message = "Статус блюда должен быть AVAILABLE или TEMPORARY_UNAVAILABLE")
        DishStatus status
) {}
