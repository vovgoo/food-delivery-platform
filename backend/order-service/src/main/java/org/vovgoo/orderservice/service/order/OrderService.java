package org.vovgoo.orderservice.service.order;

import org.vovgoo.orderservice.dto.common.PageParams;
import org.vovgoo.orderservice.dto.common.PageResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest createOrderRequest);
    PageResponse<OrderResponse> getAllOrders(PageParams pageParams);
    OrderResponse getOrderById(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest updateOrderStatusRequest);
}
