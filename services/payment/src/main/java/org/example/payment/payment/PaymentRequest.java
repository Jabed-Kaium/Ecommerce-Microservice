package org.example.payment.payment;

import lombok.Builder;
import org.example.payment.user.UserResponse;

@Builder
public record PaymentRequest(
        Long orderId,
        String orderTrackingId,
        double totalAmount,
        PaymentMethod paymentMethod,
        UserResponse user
) {
}
