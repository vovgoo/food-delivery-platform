package org.vovgoo.orderservice.service.restaurant;

import org.vovgoo.dto.dish.DishInternalResponse;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantClientService {
    RestaurantInternalResponse getRestaurant(UUID restaurantId);
    void validateRestaurant(UUID restaurantId);
    List<DishInternalResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
    List<DishInternalResponse> getAvailableDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
}
