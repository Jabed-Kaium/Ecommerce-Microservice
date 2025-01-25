package org.example.order.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
    Long userId,
    String trackingId,
    double totalAmount,
    OrderStatus status,
    PaymentMethod paymentMethod,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<OrderItem> orderItems
) {
}
