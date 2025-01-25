package org.example.notification.payment;

import lombok.Builder;
import org.example.notification.user.User;

@Builder
public record PaymentMessage(
        Long orderId,
        String orderTrackingId,
        double totalAmount,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        User user
) {
}
