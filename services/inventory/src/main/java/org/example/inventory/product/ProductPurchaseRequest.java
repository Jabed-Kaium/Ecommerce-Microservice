package org.example.inventory.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ProductPurchaseRequest(
        Long productId,
        int quantity,
        double price
) {
}
