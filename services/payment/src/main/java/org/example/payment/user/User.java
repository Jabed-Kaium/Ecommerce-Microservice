package org.example.payment.user;

import lombok.Builder;

@Builder
public record User(
        String firstName,
        String lastName,
        String email
) {
}
