package org.example.user.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record UserDto(
    @NotNull(message = "Firstname is required")
    String firstName,
    @NotNull(message = "Lastname is required")
    String lastName,
    @NotNull(message = "Email is required")
    @Email(message = "Email is not valid")
    String email
) implements Serializable{
}
