package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.common.domain.address.dto.AddressInternalResponse;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;
import org.vovgoo.common.domain.user.dto.UserInternalResponse;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.order.response.OrderShortResponse;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.Order;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class, AddressMapper.class, RestaurantMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "order.status", target = "status")
    @Mapping(source = "order.orderDate", target = "orderDate")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "order.totalPrice", target = "totalPrice")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "order.payment", target = "payment")
    OrderResponse toResponse(Order order, UserInternalResponse user, AddressInternalResponse address, RestaurantInternalResponse restaurant, List<OrderItemResponse> items);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "payment", target = "payment")
    OrderShortResponse toShortResponse(Order order);
}
