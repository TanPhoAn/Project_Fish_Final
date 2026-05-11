package com.phuoctan.controller;

import com.phuoctan.entity.OrderStatus;
import com.phuoctan.entity.Orders;
import com.phuoctan.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin/api/orders")
public class AdminOrderApiController {
    private final OrderService orderService;

    public AdminOrderApiController(OrderService orderService) {
        this.orderService = orderService;

    }

    @GetMapping
    public OrderSearchResponse searchOrders(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String status) {
        // check if status is invalid
        OrderStatus orderStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                orderStatus = null;
            }
        }

        List<Orders> ordersList = orderService.searchOrders(keyword, orderStatus);
        List<OrderSummary> items = ordersList.stream().map(order -> new OrderSummary(
                order.getId(),
                order.getCustomer().getName(),
                order.getOrder_date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                order.getTotal_price(),
                order.getStatus().name())).toList();

        return new OrderSearchResponse(items);
    }

    public record OrderSummary(
            Integer id,
            String customerName,
            String orderDate,
            long totalPrice,
            String status

    ) {
    }

    public record OrderSearchResponse(
            List<OrderSummary> items

    ) {
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer id,
            @RequestBody UpdateOrderStatusRequest request) {
        Orders order = orderService.getOrder(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(request.status());
            order.setStatus(newStatus);
            orderService.updateOrder(order);
            return ResponseEntity.ok(new OrderStatusResponse(order.getId(), order.getStatus().name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + request.status());
        }
    }

    public record UpdateOrderStatusRequest(
            String status) {
    }

    public record OrderStatusResponse(
            Integer id,
            String status) {
    }

    @GetMapping("/pending")
    public Integer pendingOrder() {
        return orderService.getPendingOrder().size();
    }
}
