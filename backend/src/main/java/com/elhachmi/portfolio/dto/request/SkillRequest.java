package com.elhachmi.portfolio.dto.request;

import com.elhachmi.portfolio.entity.enums.SkillCategory;
import com.elhachmi.portfolio.entity.enums.SkillProficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SkillRequest(
        @NotBlank(message = "Skill name is required")
        @Size(max = 255)
        String name,

        @NotNull(message = "Category is required")
        SkillCategory category,

        @NotNull(message = "Proficiency is required")
        SkillProficiency proficiency,

        Integer sortOrder
) {}
