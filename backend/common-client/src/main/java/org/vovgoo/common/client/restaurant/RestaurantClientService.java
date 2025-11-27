package org.vovgoo.common.client.restaurant;

import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantClientService {
    RestaurantInternalResponse getRestaurant(UUID restaurantId);
    List<DishInternalResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
}
