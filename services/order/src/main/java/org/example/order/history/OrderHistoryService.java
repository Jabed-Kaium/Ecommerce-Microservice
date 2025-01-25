package org.example.order.history;

import lombok.RequiredArgsConstructor;
import org.example.order.order.Order;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public void createOrderHistory(OrderHistoryRequest request) {
        OrderHistory orderHistory = OrderHistory.builder()
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .productId(request.productId())
                .quantity(request.quantity())
                .price(request.price())
                .build();

        orderHistoryRepository.save(orderHistory);
    }


}
