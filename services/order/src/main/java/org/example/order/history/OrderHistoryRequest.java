package org.example.order.history;

import jakarta.persistence.Column;
import lombok.Builder;
import org.example.order.order.Order;

@Builder
public record OrderHistoryRequest(
        Long orderId,
        Long productId,
        int quantity,
        double price
) {
}
