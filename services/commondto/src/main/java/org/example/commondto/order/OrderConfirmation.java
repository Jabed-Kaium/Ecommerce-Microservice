package org.example.commondto.order;

import lombok.Builder;
import org.example.commondto.user.UserResponse;

import java.util.List;

@Builder
public record OrderConfirmation(
        String trackingId,
        double totalAmount,
        UserResponse user,
        List<OrderItem> products
) {
}
