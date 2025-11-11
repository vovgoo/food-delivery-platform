package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "dishId", target = "dishId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    OrderItemResponse toResponse(OrderItem orderItem);
}