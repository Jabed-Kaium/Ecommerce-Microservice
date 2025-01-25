package org.example.order.order;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMapper {

    public List<OrderResponse> toOrderResponseList(List<Order> orders) {
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .userId(order.getUserId())
                        .trackingId(order.getTrackingId())
                        .totalAmount(order.getTotalAmount())
                        .paymentMethod(order.getPaymentMethod())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .updatedAt(order.getUpdatedAt())
                        .orderItems(order.getOrderItems()
                                .stream()
                                .map(orderHistory -> OrderItem.builder()
                                        .productId(orderHistory.getProductId())
                                        .quantity(orderHistory.getQuantity())
                                        .price(orderHistory.getPrice())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .userId(order.getUserId())
                .trackingId(order.getTrackingId())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(order.getOrderItems()
                        .stream()
                        .map(orderHistory -> OrderItem.builder()
                                .productId(orderHistory.getProductId())
                                .quantity(orderHistory.getQuantity())
                                .price(orderHistory.getPrice())
                                .build())
                        .toList())
                .build();
    }
}
