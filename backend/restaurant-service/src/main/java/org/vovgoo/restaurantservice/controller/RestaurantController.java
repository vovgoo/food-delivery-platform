package org.vovgoo.restaurantservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.service.dish.DishService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantService;

import java.util.UUID;

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

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable("restaurantId") UUID restaurantId) {
        return ResponseEntity.ok(restaurantService.getById(restaurantId));
    }

    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<PageResponse<DishResponse>> listDishes(@PathVariable("restaurantId") UUID restaurantId, @Valid PageParams pageParams) {
        return ResponseEntity.ok(dishService.listByRestaurant(restaurantId, pageParams));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantCreateRequest restaurantCreateRequest) {
        RestaurantResponse created = restaurantService.create(restaurantCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable UUID restaurantId, @Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest) {
        return ResponseEntity.ok(restaurantService.update(restaurantId, restaurantUpdateRequest));
    }

    @PostMapping(value = "/{restaurantId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")

    public ResponseEntity<Void> addRestaurantImage(@PathVariable UUID restaurantId, @RequestParam("file") MultipartFile file) {
        restaurantService.uploadImage(restaurantId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{restaurantId}/images/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> removeRestaurantImage(@PathVariable UUID restaurantId, @PathVariable UUID imageId) {
        restaurantService.deleteImage(restaurantId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{restaurantId}/images/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> setRestaurantProfileImage(@PathVariable UUID restaurantId, @RequestParam("file") MultipartFile file) {
        restaurantService.setProfileImage(restaurantId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{restaurantId}/images/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> removeRestaurantProfileImage(@PathVariable UUID restaurantId) {
        restaurantService.removeProfileImage(restaurantId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.delete(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/dishes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishResponse> createDish(@PathVariable UUID id, @Valid @RequestBody DishCreateRequest dishCreateRequest) {
        DishResponse created = dishService.create(id, dishCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{restaurantId}/dishes/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DishResponse> updateDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        return ResponseEntity.ok(dishService.update(restaurantId, dishId, dishUpdateRequest));
    }

    @PostMapping("/{restaurantId}/dishes/{dishId}/images")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> addDishImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @RequestParam("file") MultipartFile file) {
        dishService.uploadImage(restaurantId, dishId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{restaurantId}/dishes/{dishId}/images/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> removeDishImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @PathVariable UUID imageId) {
        dishService.deleteImage(restaurantId, dishId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{restaurantId}/dishes/{dishId}/images/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> setDishProfileImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @RequestParam("file") MultipartFile file) {
        dishService.setProfileImage(restaurantId, dishId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{restaurantId}/dishes/{dishId}/images/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> removeDishProfileImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId) {
        dishService.removeProfileImage(restaurantId, dishId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{restaurantId}/dishes/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId) {
        dishService.delete(restaurantId, dishId);
        return ResponseEntity.noContent().build();
    }
}
