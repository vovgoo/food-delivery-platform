package org.vovgoo.restaurantservice.dto.dish.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.restaurantservice.dto.image.response.ImageResponse;
import org.vovgoo.restaurantservice.entity.enums.DishStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response object for a dish")
public record DishResponse(

        @Schema(description = "Dish ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Dish name", example = "Маргарита")
        String name,

        @Schema(description = "Dish description", example = "Классическая пицца с томатным соусом и сыром")
        String description,

        @Schema(description = "Profile image of the dish")
        ImageResponse profileImage,

        @Schema(description = "Portion size in grams", example = "350")
        Integer portionInGrams,

        @Schema(description = "Proteins content in grams", example = "12.5")
        Double proteins,

        @Schema(description = "Fats content in grams", example = "10.0")
        Double fats,

        @Schema(description = "Carbohydrates content in grams", example = "45.0")
        Double carbohydrates,

        @Schema(description = "Is the dish spicy?", example = "false")
        Boolean spicy,

        @Schema(description = "Is the dish suitable for vegans?", example = "false")
        Boolean vegan,

        @Schema(description = "Is the dish suitable for vegetarians?", example = "true")
        Boolean vegetarian,

        @Schema(description = "Price of the dish", example = "450.50")
        BigDecimal price,

        @Schema(description = "Status of the dish", example = "AVAILABLE")
        DishStatus status,

        @Schema(description = "List of additional images for the dish")
        List<ImageResponse> images
) {}
