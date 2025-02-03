package org.example.notification.user;
import lombok.Builder;

@Builder
public record UserResponse(
        String firstName,
        String lastName,
        String email
) {
}
