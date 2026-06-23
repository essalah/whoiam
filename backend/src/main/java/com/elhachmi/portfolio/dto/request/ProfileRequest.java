package com.elhachmi.portfolio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255)
        String name,

        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,

        String summary,

        @Email(message = "Invalid email format")
        String email,

        @Size(max = 50)
        String phone,

        @Size(max = 255)
        String location,

        @Size(max = 500)
        String website,

        @Size(max = 500)
        String avatarUrl
) {}
