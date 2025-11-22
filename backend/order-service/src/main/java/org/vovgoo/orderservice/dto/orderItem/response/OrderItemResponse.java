package org.vovgoo.orderservice.dto.orderItem.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.orderservice.dto.dish.response.DishResponse;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Response containing details of an order item")
public record OrderItemResponse(

        @Schema(description = "Unique ID of the order item", example = "880e8400-e29b-41d4-a716-446655440333")
        UUID id,

        @Schema(description = "Details of the dish")
        DishResponse dish,

        @Schema(description = "Quantity of the dish ordered", example = "2")
        Long quantity,

        @Schema(description = "Total price for this order item", example = "12.50")
        BigDecimal price
) {}
