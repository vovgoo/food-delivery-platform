package org.vovgoo.restaurantservice.dto.restaurant.response;

import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;

import java.util.List;

public record RestaurantResponse(
        Long id,
        String name,
        String cuisine,
        String address,
        List<DishResponse> dishes
) {}
