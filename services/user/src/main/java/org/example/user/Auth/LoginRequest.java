package org.example.user.Auth;

public record LoginRequest(
        String username,
        String password
) {
}
