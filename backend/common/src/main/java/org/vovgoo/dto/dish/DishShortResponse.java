package org.vovgoo.dto.dish;

import org.vovgoo.dto.dish.enums.DishStatus;

import java.util.UUID;

public record DishShortResponse(
        UUID id,
        String name,
        String profileImageUrl,
        DishStatus status
) {}

