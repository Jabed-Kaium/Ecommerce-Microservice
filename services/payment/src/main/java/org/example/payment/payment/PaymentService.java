package org.example.payment.payment;

import lombok.RequiredArgsConstructor;
import org.example.payment.kafka.PaymentConfirmation;
import org.example.payment.kafka.PaymentProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    public void createPayment(PaymentRequest paymentRequest) {
        paymentRepository.save(
                Payment.builder()
                        .orderId(paymentRequest.orderId())
                        .orderTrackingId(paymentRequest.orderTrackingId())
                        .totalAmount(paymentRequest.totalAmount())
                        .paymentMethod(paymentRequest.paymentMethod())
                        .paymentStatus(PaymentStatus.SUCCESS)
                        .paymentDate(LocalDateTime.now())
                        .build()
        );

        paymentProducer.sendPaymentConfirmation(
                PaymentConfirmation.builder()
                        .orderId(paymentRequest.orderId())
                        .orderTrackingId(paymentRequest.orderTrackingId())
                        .totalAmount(paymentRequest.totalAmount())
                        .paymentMethod(paymentRequest.paymentMethod())
                        .paymentStatus(PaymentStatus.SUCCESS)
                        .user(paymentRequest.user())
                        .build()
        );
    }
}
