package com.elhachmi.portfolio.dto.response;

import java.util.List;

public record ProjectResponse(
        Long id,
        String title,
        String slug,
        String description,
        String imageUrl,
        String liveUrl,
        String githubUrl,
        Boolean featured,
        Integer sortOrder,
        List<String> techStack
) {}
