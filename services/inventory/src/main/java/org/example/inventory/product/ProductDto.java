package org.example.inventory.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
public record ProductDto(
        @NotNull(message = "Name is required")
        String name,
        String description,
        @Positive(message = "Available quantity must be positive")
        Long availableQuantity,
        @Positive(message = "Price must be positive")
        double price,
        @NotNull(message = "Category id is required")
        Long categoryId
) {
}
