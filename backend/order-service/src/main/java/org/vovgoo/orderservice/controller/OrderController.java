package org.vovgoo.orderservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.orderservice.dto.order.request.CreateOrderRequest;
import org.vovgoo.orderservice.dto.order.request.UpdateOrderStatusRequest;
import org.vovgoo.orderservice.dto.order.response.OrderResponse;
import org.vovgoo.orderservice.service.order.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderResponse response = orderService.placeOrder(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(@Valid PageParams pageParams) {
        return ResponseEntity.ok(orderService.getAllOrders(pageParams));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        OrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderStatusRequest updateOrderStatusRequest) {
        OrderResponse response = orderService.updateOrderStatus(orderId, updateOrderStatusRequest);
        return ResponseEntity.ok(response);
    }
}
