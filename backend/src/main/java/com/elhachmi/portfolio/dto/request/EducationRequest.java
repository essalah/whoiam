package com.elhachmi.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EducationRequest(
        @NotBlank(message = "Institution is required")
        @Size(max = 255)
        String institution,

        @NotBlank(message = "Degree is required")
        @Size(max = 255)
        String degree,

        @Size(max = 255)
        String field,

        LocalDate startDate,

        LocalDate endDate,

        String description,

        Integer sortOrder
) {}
