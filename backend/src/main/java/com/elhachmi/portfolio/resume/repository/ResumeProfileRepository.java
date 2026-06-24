package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface ResumeProfileRepository extends JpaRepository<ResumeProfile, UUID> {
    Optional<ResumeProfile> findByIdAndResumeOwnerId(UUID resumeId, UUID ownerId);
}
