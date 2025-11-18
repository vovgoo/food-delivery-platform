package org.vovgoo.restaurantservice.dto.restaurant.response;

import org.vovgoo.restaurantservice.dto.image.response.ImageResponse;
import org.vovgoo.restaurantservice.entity.enums.RestaurantStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String description,
        String cuisine,
        String address,
        String website,
        ImageResponse profileImage,
        String phone,
        LocalTime openingTime,
        LocalTime closingTime,
        Boolean deliveryAvailable,
        Boolean parkingAvailable,
        RestaurantStatus status,
        List<ImageResponse> images
) {}
