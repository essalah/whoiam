package com.elhachmi.portfolio.identity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 320, message = "Email must not exceed 320 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must contain between 8 and 72 characters")
        String password,

        @Size(max = 255, message = "Display name must not exceed 255 characters")
        String displayName,

        @NotBlank(message = "Terms version is required")
        @Size(max = 50, message = "Terms version must not exceed 50 characters")
        String termsVersion
) {
}
