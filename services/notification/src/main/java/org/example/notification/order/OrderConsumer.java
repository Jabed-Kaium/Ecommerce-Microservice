package org.example.notification.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.notification.email.EmailService;
import org.example.notification.payment.PaymentMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "order-topic", groupId = "notification-service-group")
    public void consumeOrderTopic(String rawMessage) throws JsonProcessingException {
        OrderMessage orderMessage = objectMapper.readValue(rawMessage, OrderMessage.class);
        String name = orderMessage.user().firstName() + " " + orderMessage.user().lastName();
        String subject = "Order Confirmation: " + orderMessage.trackingId();
        String to = orderMessage.user().email();
        String body = "Dear " + name + ",\n\n" +
                "Thank you for your order! Here are the details:\n" +
                "Order Tracking ID: " + orderMessage.trackingId() + "\n" +
                "Total Amount: $" + orderMessage.totalAmount() + "\n\n" +
                "Products:\n";

        for (OrderItem orderItem : orderMessage.products()) {
            body += " - Product ID: " + orderItem.productId() +
                    ", Quantity: " + orderItem.quantity() +
                    ", Price: $" + orderItem.price() + "\n";
        }

        body += "\nWe hope you enjoy your purchase!\n\nBest regards,\nYour Store Team";

        emailService.sendEmail(to, subject, body);
    }
}
