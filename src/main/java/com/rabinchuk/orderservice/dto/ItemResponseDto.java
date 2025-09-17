package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO representing a single item in the catalog")
public record ItemResponseDto(
        @Schema(description = "Unique identifier of the item", example = "1")
        Long id,

        @Schema(description = "Name of the item", example = "Laptop")
        String name,

        @Schema(description = "Price of the item", example = "1299.99")
        BigDecimal price
){
}
