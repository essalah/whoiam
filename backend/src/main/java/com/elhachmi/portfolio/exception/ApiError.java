package com.elhachmi.portfolio.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Standard API error response")
public record ApiError(
        @Schema(example = "400")
        int status,

        @Schema(example = "Bad Request")
        String error,

        @Schema(example = "VALIDATION_ERROR")
        String code,

        @Schema(example = "Request validation failed")
        String message,

        @Schema(example = "/api/v1/admin/projects")
        String path,

        Instant timestamp,

        List<FieldViolation> violations
) {
    public record FieldViolation(
            @Schema(example = "title")
            String field,

            @Schema(example = "Title is required")
            String message
    ) {
    }
}
