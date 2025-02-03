package org.example.notification.payment;

import lombok.Builder;
import org.example.notification.user.UserResponse;

@Builder
public record PaymentConfirmation(
    Long orderId,
    String orderTrackingId,
    double totalAmount,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    UserResponse user
) {
}
