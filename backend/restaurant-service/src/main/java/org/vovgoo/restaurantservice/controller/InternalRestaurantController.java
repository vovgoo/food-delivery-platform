package org.vovgoo.restaurantservice.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.dto.restaurant.RestaurantShortResponse;
import org.vovgoo.restaurantservice.service.dish.DishService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/restaurant")
@RequiredArgsConstructor
@Hidden
public class InternalRestaurantController {

    private final RestaurantService restaurantService;
    private final DishService dishService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantShortResponse> getRestaurant(@PathVariable("restaurantId") UUID restaurantId) {
        return ResponseEntity.ok(restaurantService.getInternalRestaurantById(restaurantId));
    }

    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<List<DishShortResponse>> getDishesByRestaurant( @PathVariable("restaurantId") UUID restaurantId, @RequestParam List<UUID> dishIds) {
        return ResponseEntity.ok(dishService.getInternalDishesByRestaurant(restaurantId, dishIds));
    }
}
