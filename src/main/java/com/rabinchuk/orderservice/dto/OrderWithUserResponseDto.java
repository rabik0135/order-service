package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Composite DTO combining order details with the user who placed it")
public record OrderWithUserResponseDto(
        @Schema(description = "The core order information")
        OrderResponseDto order,

        @Schema(description = "Details of the user associated with the order")
        UserResponseDto user
) {
}
