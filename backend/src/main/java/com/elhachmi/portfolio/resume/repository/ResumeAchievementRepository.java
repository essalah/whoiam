package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResumeAchievementRepository extends JpaRepository<ResumeAchievement, UUID> {
    List<ResumeAchievement> findAllByExperienceIdAndExperienceResumeOwnerIdOrderBySortOrderAsc(UUID experienceId, UUID ownerId);
}
