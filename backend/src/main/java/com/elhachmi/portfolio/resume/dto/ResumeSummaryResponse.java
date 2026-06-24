package com.elhachmi.portfolio.resume.dto;

import com.elhachmi.portfolio.resume.enums.ResumePageSize;
import com.elhachmi.portfolio.resume.enums.ResumeStatus;

import java.time.Instant;
import java.util.UUID;

public record ResumeSummaryResponse(
        UUID id,
        String dashboardTitle,
        String templateKey,
        Integer templateVersion,
        String locale,
        ResumePageSize pageSize,
        ResumeStatus status,
        Long lockVersion,
        Instant createdAt,
        Instant updatedAt
) {
}
