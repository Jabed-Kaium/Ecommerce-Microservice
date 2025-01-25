package org.example.order.payment;

import lombok.Builder;
import org.example.order.order.PaymentMethod;
import org.example.order.user.UserResponse;

@Builder
public record PaymentRequest(
    Long orderId,
    String orderTrackingId,
    double totalAmount,
    PaymentMethod paymentMethod,
    UserResponse user
) {
}
