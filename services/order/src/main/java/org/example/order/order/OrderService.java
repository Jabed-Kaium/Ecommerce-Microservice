package org.example.order.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commondto.order.OrderItem;
import org.example.order.exception.BusinessException;
import org.example.order.exception.NotFoundException;
import org.example.order.history.OrderHistoryRequest;
import org.example.order.history.OrderHistoryService;
import org.example.order.inventory.InventoryClient;
import org.example.commondto.order.OrderConfirmation;
import org.example.order.kafka.OrderProducer;
import org.example.order.payment.PaymentClient;
import org.example.order.payment.PaymentRequest;
import org.example.order.user.UserClient;
import org.example.commondto.user.UserResponse;
import org.example.order.util.TrackingIdGenerator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final InventoryClient inventoryClient;
    private final OrderHistoryService orderHistoryService;
    private final OrderProducer orderProducer;
    private final OrderMapper orderMapper;
    private final PaymentClient paymentClient;

    public OrderResponse createOrder(@Valid OrderRequest orderRequest) {
        UserResponse user = userClient.getUserById(orderRequest.userId())
                .orElseThrow(() -> new BusinessException("User not found with the specified id: " + orderRequest.userId()));

        try {
            inventoryClient.purchaseProducts(orderRequest.orderItems());
        } catch (Exception e) {
            throw new BusinessException("Inventory service error: cannot purchase products");
        }

        Order order = Order.builder()
                .userId(orderRequest.userId())
                .trackingId(TrackingIdGenerator.generateTrackingId())
                .totalAmount(orderRequest.totalAmount())
                .paymentMethod(orderRequest.paymentMethod())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .orderItems(null)
                .build();

        Order savedOrder = orderRepository.save(order);

        for(OrderItem orderItem : orderRequest.orderItems()) {
            orderHistoryService.createOrderHistory(
                    OrderHistoryRequest.builder()
                            .orderId(savedOrder.getId())
                            .productId(orderItem.productId())
                            .quantity(orderItem.quantity())
                            .price(orderItem.price())
                            .build()
            );
        }

        orderProducer.sendOrderConfirmation(
                OrderConfirmation.builder()
                        .trackingId(savedOrder.getTrackingId())
                        .totalAmount(savedOrder.getTotalAmount())
                        .user(user)
                        .products(orderRequest.orderItems())
                        .build()
        );

        paymentClient.confirmOrderPayment(
                PaymentRequest.builder()
                        .orderId(savedOrder.getId())
                        .orderTrackingId(savedOrder.getTrackingId())
                        .totalAmount(savedOrder.getTotalAmount())
                        .paymentMethod(orderRequest.paymentMethod())
                        .user(user)
                        .build()
        );

        return OrderResponse.builder()
                .userId(savedOrder.getUserId())
                .trackingId(savedOrder.getTrackingId())
                .build();
    }

    public List<OrderResponse> getAllOrders() {
        return orderMapper.toOrderResponseList(orderRepository.findAll());
    }

    @Cacheable(value = "orderCache", key = "#id")
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with the specified id: " + id));

        return orderMapper.toOrderResponse(order);
    }

    @Cacheable(value = "userOrderCache", key = "#userId")
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderMapper.toOrderResponseList(orderRepository.findByUserId(userId));
    }
}
