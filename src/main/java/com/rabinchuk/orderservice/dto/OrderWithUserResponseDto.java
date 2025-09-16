package com.rabinchuk.orderservice.dto;

public record OrderWithUserResponseDto(
        OrderResponseDto order,
        UserResponseDto user
) {
}
