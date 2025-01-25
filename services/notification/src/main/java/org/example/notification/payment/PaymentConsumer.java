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

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "notification-service-group")
    public void consumePaymentTopic(String rawMessage) throws JsonProcessingException {
        PaymentMessage paymentMessage = objectMapper.readValue(rawMessage, PaymentMessage.class);
        String name = paymentMessage.user().firstName() + " " + paymentMessage.user().lastName();
        String subject = "Payment Confirmation: " + paymentMessage.orderTrackingId();
        String to = paymentMessage.user().email();
        String body = "Dear " + name + ",\n\n" +
                "Your payment has been processed successfully!\n" +
                "Order Tracking ID: " + paymentMessage.orderTrackingId() + "\n" +
                "Payment Method: " + paymentMessage.paymentMethod() + "\n" +
                "Total Amount: $" + paymentMessage.totalAmount() + "\n" +
                "Payment Status: " + paymentMessage.paymentStatus() + "\n\n" +
                "Thank you for shopping with us!\n\nBest regards,\nYour Store Team";

        emailService.sendEmail(to, subject, body);
    }
}
