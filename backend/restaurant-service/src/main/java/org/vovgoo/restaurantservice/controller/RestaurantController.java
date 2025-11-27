package org.vovgoo.restaurantservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.common.domain.dto.exception.ExceptionResponse;
import org.vovgoo.common.domain.dto.pageable.PageParams;
import org.vovgoo.common.domain.dto.pageable.PageResponse;
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
@Tag(name = "Restaurants", description = "Restaurant and dish management")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final DishService dishService;

    @Operation(summary = "Get all restaurants", description = "Retrieve all restaurants with optional cuisine filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<RestaurantResponse>> listRestaurants(
            @Valid RestaurantSearchRequest restaurantSearchRequest,
            @Valid PageParams pageParams) {
        return ResponseEntity.ok(restaurantService.listRestaurants(restaurantSearchRequest, pageParams));
    }

    @Operation(summary = "Get restaurant by ID", description = "Retrieve a restaurant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(restaurantService.getById(restaurantId));
    }

    @Operation(summary = "Get all dishes for a restaurant", description = "Retrieve all dishes for a specific restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<PageResponse<DishResponse>> listDishes(@PathVariable UUID restaurantId,
                                                                 @Valid PageParams pageParams) {
        return ResponseEntity.ok(dishService.listByRestaurant(restaurantId, pageParams));
    }

    @Operation(summary = "Create a new restaurant", description = "Create a new restaurant (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequest restaurantCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.create(restaurantCreateRequest));
    }

    @Operation(summary = "Update a restaurant", description = "Update restaurant information (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable UUID restaurantId,
                                                               @Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest) {
        return ResponseEntity.ok(restaurantService.update(restaurantId, restaurantUpdateRequest));
    }

    @Operation(summary = "Delete a restaurant", description = "Soft delete a restaurant by setting its status to CLOSED (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.delete(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload restaurant image", description = "Upload an image for a restaurant (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping(value = "/{restaurantId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addRestaurantImage(@PathVariable UUID restaurantId,
                                                   @RequestParam("file") MultipartFile file) {
        restaurantService.uploadImage(restaurantId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove restaurant image", description = "Delete an image from a restaurant (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant or image not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{restaurantId}/images/{imageId}")
    public ResponseEntity<Void> removeRestaurantImage(@PathVariable UUID restaurantId,
                                                      @PathVariable UUID imageId) {
        restaurantService.deleteImage(restaurantId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set restaurant profile image", description = "Set a profile image for a restaurant (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile image set successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping(value = "/{restaurantId}/images/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> setRestaurantProfileImage(@PathVariable UUID restaurantId,
                                                          @RequestParam("file") MultipartFile file) {
        restaurantService.setProfileImage(restaurantId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove restaurant profile image", description = "Remove the profile image from a restaurant (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile image removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{restaurantId}/images/profile")
    public ResponseEntity<Void> removeRestaurantProfileImage(@PathVariable UUID restaurantId) {
        restaurantService.removeProfileImage(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a dish", description = "Create a new dish for a restaurant (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully",
                    content = @Content(schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/{restaurantId}/dishes")
    public ResponseEntity<DishResponse> createDish(@PathVariable UUID restaurantId,
                                                   @Valid @RequestBody DishCreateRequest dishCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dishService.create(restaurantId, dishCreateRequest));
    }

    @Operation(summary = "Update a dish", description = "Update an existing dish (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully",
                    content = @Content(schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        return ResponseEntity.ok(dishService.update(restaurantId, dishId, dishUpdateRequest));
    }

    @Operation(summary = "Delete a dish", description = "Soft delete a dish (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId) {
        dishService.delete(restaurantId, dishId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload dish image", description = "Upload an image for a dish (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish image uploaded successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/{restaurantId}/dishes/{dishId}/images")
    public ResponseEntity<Void> addDishImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @RequestParam("file") MultipartFile file) {
        dishService.uploadImage(restaurantId, dishId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove dish image", description = "Remove an image from a dish (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish image removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish or image not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{restaurantId}/dishes/{dishId}/images/{imageId}")
    public ResponseEntity<Void> removeDishImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @PathVariable UUID imageId) {
        dishService.deleteImage(restaurantId, dishId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set dish profile image", description = "Set a profile image for a dish (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile image set successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping(value = "/{restaurantId}/dishes/{dishId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> setDishProfileImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId, @RequestParam("file") MultipartFile file) {
        dishService.setProfileImage(restaurantId, dishId, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove dish profile image", description = "Remove the profile image from a dish (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile image removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User blocked or not admin",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{restaurantId}/dishes/{dishId}/profile")
    public ResponseEntity<Void> removeDishProfileImage(@PathVariable UUID restaurantId, @PathVariable UUID dishId) {
        dishService.removeProfileImage(restaurantId, dishId);
        return ResponseEntity.noContent().build();
    }
}
