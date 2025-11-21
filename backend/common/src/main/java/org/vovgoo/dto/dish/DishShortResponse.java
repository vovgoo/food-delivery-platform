package org.vovgoo.dto.dish;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.dto.dish.enums.DishStatus;

import java.util.UUID;

@Schema(description = "Short response object for a dish")
public record DishShortResponse(

        @Schema(description = "Dish ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Dish name", example = "Маргарита")
        String name,

        @Schema(description = "URL of the dish's profile image", example = "https://example.com/images/dish123.png")
        String profileImageUrl,

        @Schema(description = "Status of the dish", example = "AVAILABLE")
        DishStatus status
) {}

