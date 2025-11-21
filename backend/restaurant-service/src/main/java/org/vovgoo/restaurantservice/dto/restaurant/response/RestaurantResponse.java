package org.vovgoo.restaurantservice.dto.restaurant.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.restaurantservice.dto.image.response.ImageResponse;
import org.vovgoo.dto.restaurant.enums.RestaurantStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response object for a restaurant")
public record RestaurantResponse(

        @Schema(description = "Restaurant ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Restaurant name", example = "Итальянский дворик")
        String name,

        @Schema(description = "Restaurant description", example = "Аутентичная итальянская кухня в центре города")
        String description,

        @Schema(description = "Cuisine type", example = "Итальянская")
        String cuisine,

        @Schema(description = "Restaurant address", example = "ул. Ленина, д. 10, Москва")
        String address,

        @Schema(description = "Website URL", example = "https://italian-courtyard.ru")
        String website,

        @Schema(description = "Profile image of the restaurant")
        ImageResponse profileImage,

        @Schema(description = "Restaurant phone number", example = "+7 (495) 123-45-67")
        String phone,

        @Schema(description = "Opening time", example = "10:00")
        LocalTime openingTime,

        @Schema(description = "Closing time", example = "23:00")
        LocalTime closingTime,

        @Schema(description = "Is delivery available?", example = "true")
        Boolean deliveryAvailable,

        @Schema(description = "Is parking available?", example = "true")
        Boolean parkingAvailable,

        @Schema(description = "Restaurant status", example = "ACTIVE")
        RestaurantStatus status,

        @Schema(description = "List of additional images of the restaurant")
        List<ImageResponse> images
) {}
