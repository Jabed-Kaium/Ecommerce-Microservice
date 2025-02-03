package org.example.notification.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.notification.email.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "notification-service-group")
    public void consumePaymentTopic(PaymentConfirmation paymentConfirmation) throws JsonProcessingException {

            String name = paymentConfirmation.user().firstName() + " " + paymentConfirmation.user().lastName();
            String subject = "Payment Confirmation: " + paymentConfirmation.orderTrackingId();
            String to = paymentConfirmation.user().email();
            String body = "Dear " + name + ",\n\n" +
                    "Your payment has been processed successfully!\n" +
                    "Order Tracking ID: " + paymentConfirmation.orderTrackingId() + "\n" +
                    "Payment Method: " + paymentConfirmation.paymentMethod() + "\n" +
                    "Total Amount: $" + paymentConfirmation.totalAmount() + "\n" +
                    "Payment Status: " + paymentConfirmation.paymentStatus() + "\n\n" +
                    "Thank you for shopping with us!\n\nBest regards,\nYour Store Team";

            emailService.sendEmail(to, subject, body);
    }
}
