package com.rabinchuk.orderservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderCreatedEvent(
        Long orderId,

        Long userId,

        BigDecimal paymentAmount
) {
}
