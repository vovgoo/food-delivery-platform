package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.entity.Order;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "restaurantId", target = "restaurantId")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "payment", target = "payment")
    OrderResponse toResponse(Order order);
}