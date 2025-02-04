package org.example.user.auth;

public record LoginRequest(
        String username,
        String password
) {
}
