package com.rabinchuk.orderservice.dto;

import com.rabinchuk.orderservice.model.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentCreatedEvent(
        Long orderId,

        PaymentStatus status
) {
}
