package org.example.user.Auth;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String username,
        String password
) {
}
