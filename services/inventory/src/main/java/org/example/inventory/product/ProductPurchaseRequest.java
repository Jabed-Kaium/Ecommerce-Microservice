package org.example.inventory.product;

import lombok.Builder;

@Builder
public record ProductPurchaseRequest(
        Long productId,
        int quantity,
        double price
) {
}
