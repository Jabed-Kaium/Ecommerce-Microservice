package org.example.commondto.payment;

import lombok.Builder;
import org.example.commondto.user.UserResponse;


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
