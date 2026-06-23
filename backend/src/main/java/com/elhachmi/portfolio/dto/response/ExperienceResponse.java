package com.elhachmi.portfolio.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ExperienceResponse(
        Long id,
        String company,
        String role,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        String description,
        Integer sortOrder,
        List<String> achievements
) {}
