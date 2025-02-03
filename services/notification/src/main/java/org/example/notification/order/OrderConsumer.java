package org.example.notification.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.notification.email.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "order-topic", groupId = "notification-service-group")
    public void consumeOrderTopic(OrderConfirmation orderConfirmation) throws JsonProcessingException {

        String name = orderConfirmation.user().firstName() + " " + orderConfirmation.user().lastName();
        String subject = "Order Confirmation: " + orderConfirmation.trackingId();
        String to = orderConfirmation.user().email();
        String body = "Dear " + name + ",\n\n" +
                "Thank you for your order! Here are the details:\n" +
                "Order Tracking ID: " + orderConfirmation.trackingId() + "\n" +
                "Total Amount: $" + orderConfirmation.totalAmount() + "\n\n" +
                "Products:\n";

        for (OrderItem orderItem : orderConfirmation.products()) {
            body += " - Product ID: " + orderItem.productId() +
                    ", Quantity: " + orderItem.quantity() +
                    ", Price: $" + orderItem.price() + "\n";
        }

        body += "\nWe hope you enjoy your purchase!\n\nBest regards,\nYour Store Team";

        emailService.sendEmail(to, subject, body);
    }
}
