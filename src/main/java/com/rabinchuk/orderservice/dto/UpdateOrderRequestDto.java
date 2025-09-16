package com.rabinchuk.orderservice.dto;

import com.rabinchuk.orderservice.model.OrderStatus;
import jakarta.validation.Valid;

import java.util.List;

public record UpdateOrderRequestDto(
        OrderStatus orderStatus,

        @Valid
        List<OrderItemDto> items
) {
}
