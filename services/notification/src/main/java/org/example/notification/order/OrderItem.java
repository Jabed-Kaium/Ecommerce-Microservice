package org.example.notification.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItem(
        @NotNull(message = "Product id is required")
        Long productId,
        @Positive(message = "Quantity must be positive")
        int quantity,
        @Positive(message = "Price must be positive")
        double price
) {
}
