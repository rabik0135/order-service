package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "DTO representing user information")
public record UserResponseDto(
        @Schema(description = "Unique identifier of the user", example = "101")
        Long id,

        @Schema(description = "User's first name", example = "John")
        String name,

        @Schema(description = "User's last name", example = "Doe")
        String surname,

        @Schema(description = "User's date of birth", example = "1990-05-15")
        LocalDate birthDate,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email
) {
}
