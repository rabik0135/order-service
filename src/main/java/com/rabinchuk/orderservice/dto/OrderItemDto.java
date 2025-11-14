package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO representing an item and its quantity within an order request")
public record OrderItemDto(
        @Schema(description = "ID of the item to be ordered", example = "1")
        @NotNull(message = "Item ID is required")
        @Positive(message = "Item ID must be positive")
        Long itemId,

        @Schema(description = "Quantity of the item to be ordered", example = "2")

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Minimum quantity is 1")
        Integer quantity
) {
}
