package org.vovgoo.orderservice.service.order;

import org.vovgoo.common.domain.dto.pageable.PageParams;
import org.vovgoo.common.domain.dto.pageable.PageResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.dto.order.response.OrderShortResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest createOrderRequest);
    PageResponse<OrderShortResponse> getAllOrders(PageParams pageParams);
    OrderResponse getOrderById(UUID orderId);
    OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest updateOrderStatusRequest);
}
