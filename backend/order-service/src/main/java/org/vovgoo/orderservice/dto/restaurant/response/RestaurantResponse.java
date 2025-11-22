package org.vovgoo.orderservice.dto.restaurant.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Short response object for a restaurant")
public record RestaurantResponse(

        @Schema(description = "Restaurant ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Restaurant name", example = "Итальянский дворик")
        String name,

        @Schema(description = "Restaurant cuisine type", example = "Итальянская")
        String cuisine,

        @Schema(description = "Restaurant address", example = "ул. Ленина, д. 10, Москва")
        String address,

        @Schema(description = "Restaurant phone number", example = "+7 (495) 123-45-67")
        String phone,

        @Schema(description = "URL of the restaurant's profile image", example = "https://example.com/images/rest123.png")
        String profileImageUrl
) {}
