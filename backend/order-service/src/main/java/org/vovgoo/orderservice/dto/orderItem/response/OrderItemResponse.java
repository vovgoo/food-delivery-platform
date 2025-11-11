package org.vovgoo.orderservice.dto.orderItem.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long dishId,
        Long quantity,
        BigDecimal price
) {}
