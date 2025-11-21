package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.address.AddressInternalResponse;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.Order;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class, AddressMapper.class, RestaurantMapper.class})
public interface OrderMapper {

    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "order.userId", target = "userId")
    @Mapping(source = "order.status", target = "status")
    @Mapping(source = "order.orderDate", target = "orderDate")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "order.totalPrice", target = "totalPrice")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "order.payment", target = "payment")
    OrderResponse toResponse(Order order, AddressInternalResponse address, RestaurantInternalResponse restaurant, List<OrderItemResponse> items);
}
