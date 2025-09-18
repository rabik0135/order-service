package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "DTO for creating a new order")
public record CreateOrderRequestDto(
        @Schema(description = "ID of the user placing the order", example = "101")
        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        Long userId,

        @Schema(description = "List of items to include in the order")
        @NotEmpty(message = "An order must contains at least 1 item")
        @Valid
        List<OrderItemDto> orderItems
) {
}
