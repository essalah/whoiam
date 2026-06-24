package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResumeExperienceRepository extends JpaRepository<ResumeExperience, UUID> {
    List<ResumeExperience> findAllByResumeIdAndResumeOwnerIdOrderBySortOrderAsc(UUID resumeId, UUID ownerId);
}
