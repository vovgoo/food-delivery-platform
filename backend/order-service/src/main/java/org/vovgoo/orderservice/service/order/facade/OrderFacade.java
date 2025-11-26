package org.vovgoo.orderservice.service.order.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.dto.address.AddressInternalResponse;
import org.vovgoo.dto.dish.DishInternalResponse;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;
import org.vovgoo.dto.user.UserInternalResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.orderItem.request.AddOrderItemRequest;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.OrderItem;
import org.vovgoo.enums.order.OrderStatus;
import org.vovgoo.orderservice.exception.custom.dish.DishNotAvailableException;
import org.vovgoo.orderservice.mapper.OrderItemMapper;
import org.vovgoo.orderservice.mapper.OrderMapper;
import org.vovgoo.orderservice.service.address.AddressClientService;
import org.vovgoo.orderservice.service.restaurant.RestaurantClientService;
import org.vovgoo.user.client.UserClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final AddressClientService addressClientService;
    private final RestaurantClientService restaurantClientService;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final UserClientService userClientService;

    public Order buildOrder(UUID userId, CreateOrderRequest createOrderRequest) {
        addressClientService.validateAddress(userId, createOrderRequest.deliveryAddress());
        restaurantClientService.validateRestaurant(createOrderRequest.restaurantId());
        List<DishInternalResponse> dishes = restaurantClientService.getAvailableDishesByRestaurant(
                createOrderRequest.restaurantId(),
                createOrderRequest.items().stream().map(AddOrderItemRequest::dishId).toList()
        );
        Map<UUID, DishInternalResponse> dishesMap = dishes.stream()
                .collect(Collectors.toMap(DishInternalResponse::id, d -> d));

        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .userId(userId)
                .restaurantId(createOrderRequest.restaurantId())
                .deliveryAddress(createOrderRequest.deliveryAddress())
                .totalPrice(BigDecimal.ZERO)
                .build();

        List<OrderItem> orderItems = createOrderRequest.items().stream()
            .map(itemRequest -> {
                DishInternalResponse dish = dishesMap.get(itemRequest.dishId());
                if (dish == null) throw new DishNotAvailableException();

                return OrderItem.builder()
                        .dishId(dish.id())
                        .quantity(itemRequest.quantity())
                        .price(dish.price())
                        .order(order)
                        .build();
            })
            .toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        return order;
    }

    public OrderResponse assembleOrderResponse(Order order) {
        UserInternalResponse user = userClientService.getUser(order.getUserId());
        AddressInternalResponse address = addressClientService.getAddress(order.getUserId(), order.getDeliveryAddress());
        RestaurantInternalResponse restaurant = restaurantClientService.getRestaurant(order.getRestaurantId());
        List<DishInternalResponse> dishes = restaurantClientService.getDishesByRestaurant(
                order.getRestaurantId(),
                order.getItems().stream().map(OrderItem::getDishId).toList()
        );

        List<OrderItemResponse> orderItemResponses = orderItemMapper.toResponseList(order.getItems(), dishes);

        return orderMapper.toResponse(order, user, address, restaurant, orderItemResponses);
    }
}

