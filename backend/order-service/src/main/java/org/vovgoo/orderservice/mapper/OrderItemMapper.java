package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = DishMapper.class)
public interface OrderItemMapper {

    @Mapping(source = "orderItem.id", target = "id")
    @Mapping(source = "dish", target = "dish")
    @Mapping(source = "orderItem.quantity", target = "quantity")
    @Mapping(source = "orderItem.price", target = "price")
    OrderItemResponse toResponse(OrderItem orderItem, DishShortResponse dish);

    default List<OrderItemResponse> toResponseList(List<OrderItem> orderItems, List<DishShortResponse> dishes) {
        return orderItems.stream()
                .map(oi -> {
                    DishShortResponse dish = dishes.stream()
                            .filter(d -> d.id().equals(oi.getDishId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException(
                                    "Dish not found for OrderItem " + oi.getId()
                            ));
                    return toResponse(oi, dish);
                })
                .collect(Collectors.toList());
    }
}
