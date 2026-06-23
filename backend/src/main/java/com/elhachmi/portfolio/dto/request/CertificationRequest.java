package com.elhachmi.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CertificationRequest(
        @NotBlank(message = "Certification name is required")
        @Size(max = 255)
        String name,

        @Size(max = 255)
        String issuer,

        LocalDate issueDate,

        @Size(max = 500)
        String url,

        Integer sortOrder
) {}
