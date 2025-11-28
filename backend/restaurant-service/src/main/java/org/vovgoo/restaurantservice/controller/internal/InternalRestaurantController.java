package org.vovgoo.restaurantservice.controller.internal;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;
import org.vovgoo.restaurantservice.service.dish.DishService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/restaurants")
@RequiredArgsConstructor
@Hidden
public class InternalRestaurantController {

    private final RestaurantService restaurantService;
    private final DishService dishService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantInternalResponse> getRestaurant(@PathVariable("restaurantId") UUID restaurantId) {
        return ResponseEntity.ok(restaurantService.getInternalRestaurantById(restaurantId));
    }

    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<List<DishInternalResponse>> getDishesByRestaurant(@PathVariable("restaurantId") UUID restaurantId, @RequestParam List<UUID> dishIds) {
        return ResponseEntity.ok(dishService.getInternalDishesByRestaurant(restaurantId, dishIds));
    }
}
