package org.example.order.kafka;

import lombok.Builder;
import org.example.order.order.OrderItem;
import org.example.order.user.UserResponse;

import java.util.List;

@Builder
public record OrderConfirmation(
        String trackingId,
        double totalAmount,
        UserResponse user,
        List<OrderItem> products
) {
}
