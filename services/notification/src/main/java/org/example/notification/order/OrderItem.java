package org.example.notification.order;

import lombok.Builder;

@Builder
public record OrderItem(
        Long productId,
        int quantity,
        double price
) {
}
