package com.elhachmi.portfolio.dto.request;

import com.elhachmi.portfolio.entity.enums.LanguageProficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LanguageRequest(
        @NotBlank(message = "Language name is required")
        String name,

        @NotNull(message = "Proficiency is required")
        LanguageProficiency proficiency
) {}
