package org.example.notification.order;

import lombok.Builder;
import org.example.notification.user.User;

import java.util.List;

@Builder
public record OrderMessage(
        String trackingId,
        double totalAmount,
        User user,
        List<OrderItem> products
) {
}
