package org.example.order.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(
  Long userId,
  double totalAmount,
  PaymentMethod paymentMethod,
  @NotEmpty(message = "Order items cannot be empty")
  List<OrderItem> orderItems
) {
}
