package com.rabinchuk.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDto(
        @NotNull(message = "Item ID is required")
        @Positive(message = "Item ID must be positive")
        Long itemId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Minimum quantity is 1")
        Integer quantity
) {
}
