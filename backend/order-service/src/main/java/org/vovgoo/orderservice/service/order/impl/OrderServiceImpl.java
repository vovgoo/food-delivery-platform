package org.vovgoo.orderservice.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.orderservice.domain.kafka.key.KafkaEvents;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.order.response.OrderShortResponse;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.orderservice.exception.custom.order.OrderNotFoundException;
import org.vovgoo.orderservice.mapper.OrderMapper;
import org.vovgoo.orderservice.repository.OrderRepository;
import org.vovgoo.orderservice.service.kafka.KafkaEventPublisher;
import org.vovgoo.orderservice.domain.kafka.event.OrderCreatedEvent;
import org.vovgoo.orderservice.domain.kafka.event.OrderStatusChangedEvent;
import org.vovgoo.orderservice.service.order.OrderService;
import org.vovgoo.orderservice.service.order.facade.OrderFacade;
import org.vovgoo.orderservice.service.payment.PaymentService;
import org.vovgoo.security.utils.CurrentUserUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderFacade orderFacade;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest createOrderRequest) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        Order order = orderFacade.buildOrder(userId, createOrderRequest);

        Payment payment = paymentService.processPayment(order, createOrderRequest.payment());
        order.setPayment(payment);

        order = orderRepository.save(order);

        OrderResponse orderResponse = orderFacade.assembleOrderResponse(order);

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(orderResponse.id())
                .userId(orderResponse.user().id())
                .userPhone(orderResponse.user().phone())
                .orderDate(orderResponse.orderDate())
                .totalPrice(orderResponse.totalPrice())
                .build();

        kafkaEventPublisher.publish(KafkaEvents.ORDER_CREATED, orderCreatedEvent);

        return orderResponse;
    }

    @Override
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
    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.setStatus(updateOrderStatusRequest.status());
        order = orderRepository.save(order);

        OrderResponse orderResponse = orderFacade.assembleOrderResponse(order);

        OrderStatusChangedEvent orderStatusChangedEvent = OrderStatusChangedEvent.builder()
                .orderId(orderResponse.id())
                .userId(orderResponse.user().id())
                .userPhone(orderResponse.user().phone())
                .orderStatus(orderResponse.status())
                .build();

        kafkaEventPublisher.publish(KafkaEvents.ORDER_STATUS_CHANGED, orderStatusChangedEvent);

        return orderResponse;
    }
}
