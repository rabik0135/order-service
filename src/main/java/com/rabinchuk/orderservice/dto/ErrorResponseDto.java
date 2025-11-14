package com.rabinchuk.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Schema(description = "Standardized error response object")
public record ErrorResponseDto(
        @Schema(description = "The path of the request that caused the error", example = "/api/users/99")
        String path,

        @Schema(description = "A human-readable error message", example = "User not found with id: 99")
        String errorMessage,

        @Schema(description = "The HTTP status code", example = "404")
        int statusCode,

        @Schema(description = "Timestamp of when the error occurred")
        LocalDateTime timestamp,

        @Schema(description = "A map of validation errors, where the key is the field name and the value is the error message",
                example = "{\"email\": \"Invalid email format\", \"name\": \"Name is required\"}", nullable = true)
        Map<String, String> errors
) {
}
