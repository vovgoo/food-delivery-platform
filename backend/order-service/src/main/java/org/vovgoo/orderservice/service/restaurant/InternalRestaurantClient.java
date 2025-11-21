package org.vovgoo.orderservice.service.restaurant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.dto.restaurant.RestaurantShortResponse;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "restaurant-service", path = "/internal/restaurant")
public interface InternalRestaurantClient {

    @GetMapping("/{restaurantId}")
    RestaurantShortResponse getRestaurant(@PathVariable("restaurantId") UUID restaurantId);

    @GetMapping("/{restaurantId}/dishes")
    List<DishShortResponse> getDishesByRestaurant(@PathVariable("restaurantId") UUID restaurantId, @RequestParam List<UUID> dishIds);
}
