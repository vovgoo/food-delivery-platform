package org.vovgoo.orderservice.service.order.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.vovgoo.orderservice.dto.common.PageParams;
import org.vovgoo.orderservice.dto.common.PageResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.OrderItem;
import org.vovgoo.orderservice.entity.enums.OrderStatus;
import org.vovgoo.orderservice.mapper.OrderMapper;
import org.vovgoo.orderservice.repository.OrderRepository;
import org.vovgoo.orderservice.service.order.OrderService;
import org.vovgoo.orderservice.service.payment.PaymentService;
import org.vovgoo.orderservice.utils.SecurityUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse placeOrder(CreateOrderRequest createOrderRequest) {
        Long userId = SecurityUtils.getCurrentUserId();

        List<OrderItem> orderItems = createOrderRequest.items().stream()
                .map(item -> OrderItem.builder()
                        .dishId(item.dishId())
                        .quantity(item.quantity())
                        .price(item.price())
                        .build())
                .toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(userId)
                .restaurantId(createOrderRequest.restaurantId())
                .status(OrderStatus.CREATED)
                .totalPrice(totalPrice)
                .items(orderItems)
                .build();

        orderItems.forEach(i -> i.setOrder(order));
        orderRepository.save(order);

        paymentService.processPayment(order, createOrderRequest.payment());

        return orderMapper.toResponse(order);
    }

    @Override
    public PageResponse<OrderResponse> getAllOrders(PageParams pageParams) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean isAdmin = SecurityUtils.isAdmin();

        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());

        Page<Order> orderPage = isAdmin ? orderRepository.findAll(pageRequest) : orderRepository.findByUserId(userId, pageRequest);

        Page<OrderResponse> content = orderPage.map(orderMapper::toResponse);

        return PageResponse.of(content);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean isAdmin = SecurityUtils.isAdmin();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));

        if (!isAdmin && !order.getUserId().equals(userId)) {
            throw new AccessDeniedException("Доступ запрещён");
        }

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));

        order.setStatus(updateOrderStatusRequest.status());
        orderRepository.save(order);

        return orderMapper.toResponse(order);
    }
}
