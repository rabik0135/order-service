package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "DTO for creating or updating an item")
public record ItemRequestDto(
        @Schema(description = "Name of the item", example = "Laptop")
        @NotBlank(message = "Item name is required")
        @Size(min = 2, max = 50, message = "Item name must be between 2 and 50 characters")
        String name,

        @Schema(description = "Price of the item", example = "1299.99")
        @NotNull(message = "Item price is required")
        @Positive(message = "Item price should be positive")
        BigDecimal price
) {
}
