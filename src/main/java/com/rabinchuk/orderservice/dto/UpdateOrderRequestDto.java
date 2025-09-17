package com.rabinchuk.orderservice.dto;

import com.rabinchuk.orderservice.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.List;

@Schema(description = "DTO for updating an existing order. All fields are optional.")
public record UpdateOrderRequestDto(
        @Schema(description = "The new status for the order", example = "DELIVERED")
        OrderStatus orderStatus,

        @Schema(description = "A new list of items to completely replace the old list")
        @Valid
        List<OrderItemDto> items
) {}
