package com.elhachmi.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ExperienceRequest(
        @NotBlank(message = "Company is required")
        @Size(max = 255)
        String company,

        @NotBlank(message = "Role is required")
        @Size(max = 255)
        String role,

        LocalDate startDate,

        LocalDate endDate,

        @Size(max = 255)
        String location,

        String description,

        Integer sortOrder,

        List<String> achievements
) {}
