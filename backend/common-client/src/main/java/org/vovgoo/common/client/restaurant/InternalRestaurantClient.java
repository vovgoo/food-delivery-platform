package org.vovgoo.common.client.restaurant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "restaurant-service",
        contextId = "internalRestaurantClient",
        path = "/internal/restaurants"
)
public interface InternalRestaurantClient {

    @GetMapping("/{restaurantId}")
    RestaurantInternalResponse getRestaurant(@PathVariable("restaurantId") UUID restaurantId);

    @GetMapping("/{restaurantId}/dishes")
    List<DishInternalResponse> getDishesByRestaurant(@PathVariable("restaurantId") UUID restaurantId, @RequestParam("dishIds") List<UUID> dishIds);
}
