package org.vovgoo.orderservice.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.dto.address.AddressShortResponse;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.dto.restaurant.RestaurantShortResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.orderItem.request.AddOrderItemRequest;
import org.vovgoo.orderservice.dto.orderItem.response.OrderItemResponse;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.OrderItem;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.orderservice.entity.enums.OrderStatus;
import org.vovgoo.orderservice.exception.custom.dish.DishNotAvailableException;
import org.vovgoo.orderservice.mapper.OrderItemMapper;
import org.vovgoo.orderservice.mapper.OrderMapper;
import org.vovgoo.orderservice.repository.OrderRepository;
import org.vovgoo.orderservice.service.address.AddressService;
import org.vovgoo.orderservice.service.order.OrderService;
import org.vovgoo.orderservice.service.payment.PaymentService;
import org.vovgoo.orderservice.service.restaurant.RestaurantService;
import org.vovgoo.security.utils.CurrentUserUtils;
import org.vovgoo.user.aspect.CheckUserStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressService addressService;
    private final RestaurantService restaurantService;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @CheckUserStatus
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest createOrderRequest) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        AddressShortResponse address = addressService.getValidAddress(userId, createOrderRequest.deliveryAddress());
        RestaurantShortResponse restaurant = restaurantService.getRestaurantValid(createOrderRequest.restaurantId());
        List<DishShortResponse> dishes = restaurantService.getAvailableDishesByRestaurant(
                createOrderRequest.restaurantId(),
                createOrderRequest.items().stream().map(AddOrderItemRequest::dishId).toList()
        );

        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .userId(userId)
                .restaurantId(createOrderRequest.restaurantId())
                .deliveryAddress(createOrderRequest.deliveryAddress())
                .totalPrice(BigDecimal.ZERO)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (AddOrderItemRequest itemRequest : createOrderRequest.items()) {
            DishShortResponse dish = dishes.stream()
                    .filter(d -> d.id().equals(itemRequest.dishId()))
                    .findFirst()
                    .orElseThrow(DishNotAvailableException::new);

            OrderItem item = OrderItem.builder()
                    .dishId(dish.id())
                    .quantity(itemRequest.quantity())
                    .price(dish.price())
                    .order(order)
                    .build();

            orderItems.add(item);
            totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Payment payment = paymentService.processPayment(order, createOrderRequest.payment());

        order.setPayment(payment);

        order = orderRepository.save(order);

        List<OrderItemResponse> orderItemResponses = orderItemMapper.toResponseList(order.getItems(), dishes);

        return orderMapper.toResponse(order, address,  restaurant, orderItemResponses);
    }

    @Override
    @CheckUserStatus
    public PageResponse<OrderResponse> getAllOrders(PageParams pageParams) {
//        Long userId = SecurityUtils.getCurrentUserId();
//        boolean isAdmin = SecurityUtils.isAdmin();
//
//        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());
//
//        Page<Order> orderPage = isAdmin ? orderRepository.findAll(pageRequest) : orderRepository.findByUserId(userId, pageRequest);
//
//        Page<OrderResponse> content = orderPage.map(orderMapper::toResponse);
//
//        return PageResponse.of(content);
        return null;
    }

    @Override
    @CheckUserStatus
    public OrderResponse getOrderById(Long orderId) {
//        Long userId = SecurityUtils.getCurrentUserId();
//        boolean isAdmin = SecurityUtils.isAdmin();
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
//
//        if (!isAdmin && !order.getUserId().equals(userId)) {
//            throw new AccessDeniedException("Доступ запрещён");
//        }
//
//        return orderMapper.toResponse(order);

        return null;
    }

    @Override
    @CheckUserStatus
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
//
//        order.setStatus(updateOrderStatusRequest.status());
//        orderRepository.save(order);
//
//        return orderMapper.toResponse(order);
        return null;
    }
}
