package org.example.order.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.Serializable;

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
