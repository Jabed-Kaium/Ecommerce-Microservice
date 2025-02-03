package org.example.commondto.user;
import lombok.Builder;

@Builder
public record UserResponse(
        String firstName,
        String lastName,
        String email
) {
}
