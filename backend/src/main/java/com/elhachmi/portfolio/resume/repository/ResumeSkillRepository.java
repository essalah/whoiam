package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, UUID> {
    List<ResumeSkill> findAllByResumeIdAndResumeOwnerIdOrderBySortOrderAsc(UUID resumeId, UUID ownerId);
}
