package org.vovgoo.restaurantservice.dto.restaurant.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for searching restaurants by cuisine")
public record RestaurantSearchRequest (

        @Schema(description = "Cuisine to filter restaurants by", example = "Итальянская")
        String cuisine
) {}
