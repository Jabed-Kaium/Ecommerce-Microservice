package org.example.inventory.category;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record CategoryDto(
        @NotNull(message = "Name is required")
        String name,
        String description
) implements Serializable {
}
