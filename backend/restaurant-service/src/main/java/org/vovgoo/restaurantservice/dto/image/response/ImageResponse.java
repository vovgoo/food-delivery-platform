package org.vovgoo.restaurantservice.dto.image.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response object for an image")
public record ImageResponse(

        @Schema(description = "Image ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "URL of the image", example = "https://example.com/images/restaurant-profile.jpg")
        String url
) {}
