package com.elhachmi.portfolio.resume.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateResumeRequest(
        @NotBlank(message = "Dashboard title is required")
        @Size(max = 255, message = "Dashboard title must not exceed 255 characters")
        String dashboardTitle
) {
}
