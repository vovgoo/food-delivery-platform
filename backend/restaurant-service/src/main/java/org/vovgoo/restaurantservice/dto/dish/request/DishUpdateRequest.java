package org.vovgoo.restaurantservice.dto.dish.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record DishUpdateRequest(

        @NotBlank(message = "Название блюда не может быть пустым")
        @Size(min = 2, max = 100, message = "Название блюда должно быть от 2 до 100 символов")
        String name,

        @Size(max = 500, message = "Описание блюда должно быть до 500 символов")
        String description,

        @NotNull(message = "Цена блюда обязательна")
        @DecimalMin(value = "0.01", message = "Цена блюда должна быть больше 0")
        @Digits(integer = 6, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
        BigDecimal price,

        @URL(message = "URL изображения должен быть валидным")
        String imageUrl,

        @NotNull(message = "Id ресторана обязателен")
        Long restaurantId
) {}
