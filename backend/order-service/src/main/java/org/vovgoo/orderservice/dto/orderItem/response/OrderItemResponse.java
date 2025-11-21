package org.vovgoo.orderservice.dto.orderItem.response;

import org.vovgoo.orderservice.dto.dish.response.DishResponse;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        DishResponse dish,
        Long quantity,
        BigDecimal price
) {}
