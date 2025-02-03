package org.example.notification.order;

import lombok.Builder;
import org.example.notification.user.UserResponse;

import java.util.List;

@Builder
public record OrderConfirmation(
        String trackingId,
        double totalAmount,
        UserResponse user,
        List<OrderItem> products
) {
}
