package org.example.notification.user;

import lombok.Builder;

@Builder
public record User(
        String firstName,
        String lastName,
        String email
) {
}
