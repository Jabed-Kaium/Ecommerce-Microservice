package org.example.payment.user;
import lombok.Builder;

@Builder
public record UserResponse(
        String firstName,
        String lastName,
        String email
) {
}
