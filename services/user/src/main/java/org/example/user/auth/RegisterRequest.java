package org.example.user.auth;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String username,
        String password
) {
}
