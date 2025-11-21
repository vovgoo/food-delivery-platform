package org.vovgoo.dto.dish;

import org.vovgoo.dto.dish.enums.DishStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record DishShortResponse(
        UUID id,
        String name,
        String profileImageUrl,
        BigDecimal price,
        DishStatus status
) {}

