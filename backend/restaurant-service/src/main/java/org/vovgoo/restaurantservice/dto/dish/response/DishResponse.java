package org.vovgoo.restaurantservice.dto.dish.response;

import org.vovgoo.restaurantservice.dto.image.response.ImageResponse;
import org.vovgoo.restaurantservice.entity.enums.DishStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DishResponse(
        UUID id,
        String name,
        String description,
        ImageResponse profileImage,
        Integer portionInGrams,
        Double proteins,
        Double fats,
        Double carbohydrates,
        Boolean spicy,
        Boolean vegan,
        Boolean vegetarian,
        BigDecimal price,
        DishStatus status,
        List<ImageResponse> images
) { }
