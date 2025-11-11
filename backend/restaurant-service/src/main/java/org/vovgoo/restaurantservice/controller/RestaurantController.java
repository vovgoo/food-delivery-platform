package org.vovgoo.restaurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.restaurantservice.dto.common.PageParams;
import org.vovgoo.restaurantservice.dto.common.PageResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.service.DishService;
import org.vovgoo.restaurantservice.service.RestaurantService;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final DishService dishService;

    @GetMapping
    public ResponseEntity<PageResponse<RestaurantResponse>> listRestaurants(@Valid RestaurantSearchRequest restaurantSearchRequest, @Valid PageParams pageParams) {
        return ResponseEntity.ok(restaurantService.listRestaurants(restaurantSearchRequest, pageParams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @GetMapping("/{id}/dishes")
    public ResponseEntity<PageResponse<DishResponse>> listDishes(@PathVariable Long id, @Valid PageParams pageParams) {
        return ResponseEntity.ok(dishService.listByRestaurant(id, pageParams));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantCreateRequest restaurantCreateRequest) {
        RestaurantResponse created = restaurantService.create(restaurantCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest) {
        return ResponseEntity.ok(restaurantService.update(id, restaurantUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/dishes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishResponse> createDish(@PathVariable Long id, @Valid @RequestBody DishCreateRequest dishCreateRequest) {
        DishResponse created = dishService.create(id, dishCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{restaurantId}/dishes/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long restaurantId, @PathVariable Long dishId, @Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        return ResponseEntity.ok(dishService.update(restaurantId, dishId, dishUpdateRequest));
    }

    @DeleteMapping("/{restaurantId}/dishes/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDish(@PathVariable Long restaurantId, @PathVariable Long dishId) {
        dishService.delete(restaurantId, dishId);
        return ResponseEntity.noContent().build();
    }
}
