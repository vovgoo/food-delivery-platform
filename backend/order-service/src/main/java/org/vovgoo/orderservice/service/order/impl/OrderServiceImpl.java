package org.vovgoo.orderservice.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.order.response.OrderShortResponse;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.orderservice.exception.custom.order.OrderNotFoundException;
import org.vovgoo.orderservice.mapper.OrderMapper;
import org.vovgoo.orderservice.repository.OrderRepository;
import org.vovgoo.orderservice.service.order.OrderService;
import org.vovgoo.orderservice.service.order.facade.OrderFacade;
import org.vovgoo.orderservice.service.payment.PaymentService;
import org.vovgoo.security.utils.CurrentUserUtils;
import org.vovgoo.user.aspect.CheckUserStatus;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderFacade orderFacade;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Override
    @CheckUserStatus
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest createOrderRequest) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        Order order = orderFacade.buildOrder(userId, createOrderRequest);

        Payment payment = paymentService.processPayment(order, createOrderRequest.payment());
        order.setPayment(payment);

        order = orderRepository.save(order);

        return orderFacade.assembleOrderResponse(order);
    }

    @Override
    @CheckUserStatus
    public PageResponse<OrderShortResponse> getAllOrders(PageParams pageParams) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());

        Page<Order> orders;

        if(CurrentUserUtils.isAdmin()) {
            orders = orderRepository.findAllWithItemsAndPayment(pageRequest);
        } else {
            orders = orderRepository.findByUserIdWithItemsAndPayment(userId, pageRequest);
        }

        Page<OrderShortResponse> orderResponses = orders.map(orderMapper::toShortResponse);

        return PageResponse.of(orderResponses);
    }

    @Override
    @CheckUserStatus
    public OrderResponse getOrderById(UUID orderId) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if(!order.getUserId().equals(userId) && !CurrentUserUtils.isAdmin()) {
            throw new AccessDeniedException("Доступ запрещен.");
        }

        return orderFacade.assembleOrderResponse(order);
    }

    @Override
    @CheckUserStatus
    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.setStatus(updateOrderStatusRequest.status());
        order = orderRepository.save(order);

        return orderFacade.assembleOrderResponse(order);
    }
}
