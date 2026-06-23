package com.elhachmi.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProjectRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,

        String description,

        @Size(max = 500)
        String imageUrl,

        @Size(max = 500)
        String liveUrl,

        @Size(max = 500)
        String githubUrl,

        Boolean featured,

        Integer sortOrder,

        List<Long> techStackIds
) {}
