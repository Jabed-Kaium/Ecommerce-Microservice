package org.example.inventory.category;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryDto(
        @NotNull(message = "Name is required")
        String name,
        String description
) {
}
