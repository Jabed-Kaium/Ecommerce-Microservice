package org.example.order.order;

import lombok.Builder;
import org.example.commondto.order.OrderItem;
import org.example.commondto.payment.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
    Long userId,
    String trackingId,
    double totalAmount,
    OrderStatus status,
    PaymentMethod paymentMethod,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<OrderItem> orderItems
) {
}
