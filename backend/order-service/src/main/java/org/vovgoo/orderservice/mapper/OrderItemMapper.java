package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.OrderItem;

@Mapper(componentModel = "spring", uses = DishMapper.class)
public interface OrderItemMapper {

    @Mapping(source = "orderItem.id", target = "id")
    @Mapping(source = "dish", target = "dish")
    @Mapping(source = "orderItem.quantity", target = "quantity")
    @Mapping(source = "orderItem.price", target = "price")
    OrderItemResponse toResponse(OrderItem orderItem, DishShortResponse dish);
}
