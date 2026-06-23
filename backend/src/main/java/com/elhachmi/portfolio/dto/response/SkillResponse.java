package com.elhachmi.portfolio.dto.response;

public record SkillResponse(
        Long id,
        String name,
        String category,
        String proficiency,
        Integer sortOrder
) {}
