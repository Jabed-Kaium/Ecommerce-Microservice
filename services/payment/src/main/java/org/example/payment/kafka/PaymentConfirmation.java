package org.example.payment.kafka;

import lombok.Builder;
import org.example.payment.payment.PaymentMethod;
import org.example.payment.payment.PaymentStatus;
import org.example.payment.user.User;

@Builder
public record PaymentConfirmation(
    Long orderId,
    String orderTrackingId,
    double totalAmount,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    User user
) {
}
