package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumePublication;
import com.elhachmi.portfolio.resume.enums.PublicationState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResumePublicationRepository extends JpaRepository<ResumePublication, UUID> {
    Optional<ResumePublication> findByResumeIdAndResumeOwnerId(UUID resumeId, UUID ownerId);
    Optional<ResumePublication> findByPublicSlugAndStateAndResumeDeletedAtIsNull(String publicSlug, PublicationState state);
}
