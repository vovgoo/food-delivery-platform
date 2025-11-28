package org.vovgoo.common.domain.restaurant.dto;

import org.vovgoo.common.domain.restaurant.enums.RestaurantStatus;

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
