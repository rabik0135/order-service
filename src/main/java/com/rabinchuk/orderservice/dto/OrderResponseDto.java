package com.rabinchuk.orderservice.dto;

import com.rabinchuk.orderservice.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long userId,
        OrderStatus status,
        LocalDateTime creationDate,
        List<ItemResponseDto> items
) {
}
