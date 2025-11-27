package org.vovgoo.common.domain.dish.dto;

import org.vovgoo.common.domain.dish.enums.DishStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record DishInternalResponse(
        UUID id,
        String name,
        String profileImageUrl,
        BigDecimal price,
        DishStatus status
) {}

