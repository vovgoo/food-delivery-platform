package org.vovgoo.dto.restaurant;

import org.vovgoo.dto.restaurant.enums.RestaurantStatus;

import java.util.UUID;

public record RestaurantShortResponse(
        UUID id,
        String name,
        String cuisine,
        String address,
        String phone,
        String profileImageUrl,
        RestaurantStatus status
) {}
