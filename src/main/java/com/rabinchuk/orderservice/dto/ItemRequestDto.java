package com.rabinchuk.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ItemRequestDto(
        @NotBlank(message = "Item name is required")
        @Size(min = 2, max = 50, message = "Item name must be between 2 and 50 characters")
        String name,

        @NotNull(message = "Item price is required")
        @Positive(message = "Item price should be positive")
        BigDecimal price
) {
}
