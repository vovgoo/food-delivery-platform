package org.vovgoo.orderservice.service.restaurant;

import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantInternalResponse getRestaurant(UUID restaurantId);
    void validateRestaurant(UUID restaurantId);
    List<DishInternalResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
    List<DishInternalResponse> getAvailableDishes(UUID restaurantId, List<UUID> dishIds);
}
