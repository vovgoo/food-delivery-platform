package org.vovgoo.orderservice.service.restaurant;

import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.dto.restaurant.RestaurantShortResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantShortResponse getRestaurant(UUID restaurantId);
    void validateRestaurant(UUID restaurantId);
    List<DishShortResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
    List<DishShortResponse> getAvailableDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
}
