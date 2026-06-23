package com.elhachmi.portfolio.dto.response;

import java.time.LocalDate;

public record EducationResponse(
        Long id,
        String institution,
        String degree,
        String field,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        Integer sortOrder
) {}
