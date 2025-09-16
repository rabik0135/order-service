package com.rabinchuk.orderservice.dto;

import java.math.BigDecimal;

public record ItemResponseDto(
        Long id,
        String name,
        BigDecimal price
){
}
