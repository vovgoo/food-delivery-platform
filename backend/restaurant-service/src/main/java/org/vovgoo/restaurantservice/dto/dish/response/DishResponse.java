package org.vovgoo.restaurantservice.dto.dish.response;

import java.math.BigDecimal;

public record DishResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl
) {}
