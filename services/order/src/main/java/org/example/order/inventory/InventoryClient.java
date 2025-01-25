package org.example.order.inventory;

import org.example.order.order.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        name = "inventory-service",
        url = "${application.config.product-url}"
)
public interface InventoryClient {

    @PostMapping("/purchase")
    void purchaseProducts(List<OrderItem> orderItems);
}
