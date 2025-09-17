package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a single item line within an order response")
public record OrderItemResponseDto(
        @Schema(description = "The details of the item")
        ItemResponseDto item,

        @Schema(description = "Quantity of this item in the order", example = "3")
        Integer quantity
) {}
