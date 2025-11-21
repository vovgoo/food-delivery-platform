package org.vovgoo.orderservice.service.restaurant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.vovgoo.dto.dish.DishInternalResponse;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "restaurant-service",
        contextId = "internalRestaurantClient",
        path = "/internal/restaurant"
)
public interface InternalRestaurantClient {

    @GetMapping("/{restaurantId}")
    RestaurantInternalResponse getRestaurant(@PathVariable("restaurantId") UUID restaurantId);

    @GetMapping("/{restaurantId}/dishes")
    List<DishInternalResponse> getDishesByRestaurant(@PathVariable("restaurantId") UUID restaurantId, @RequestParam List<UUID> dishIds);
}
