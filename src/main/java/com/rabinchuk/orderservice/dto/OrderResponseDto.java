package com.rabinchuk.orderservice.dto;

import com.rabinchuk.orderservice.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "DTO representing the core details of an order")
public record OrderResponseDto(
        @Schema(description = "Unique identifier of the order", example = "55")
        Long id,

        @Schema(description = "ID of the user who placed the order", example = "101")
        Long userId,

        @Schema(description = "Current status of the order", example = "PROCESSING")
        OrderStatus status,

        @Schema(description = "Timestamp of when the order was created")
        LocalDateTime creationDate,

        @Schema(description = "List of items included in the order")
        List<OrderItemResponseDto> items
) {
}
