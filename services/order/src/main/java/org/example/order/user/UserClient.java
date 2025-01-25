package org.example.order.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
    name = "user-service",
    url = "${application.config.user-url}"
)
public interface UserClient {

    @GetMapping("/{id}")
    Optional<UserResponse> getUserById(@PathVariable("id") Long id);
}
