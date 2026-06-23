package com.elhachmi.portfolio.dto.request;

import com.elhachmi.portfolio.entity.enums.SocialPlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SocialLinkRequest(
        @NotNull(message = "Platform is required")
        SocialPlatform platform,

        @NotBlank(message = "URL is required")
        @Size(max = 500)
        String url
) {}
