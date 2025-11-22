package org.vovgoo.dto.restaurant;

import org.vovgoo.enums.restaurant.RestaurantStatus;

import java.util.UUID;

public record RestaurantInternalResponse(
        UUID id,
        String name,
        String cuisine,
        String address,
        String phone,
        String profileImageUrl,
        RestaurantStatus status
) {}
