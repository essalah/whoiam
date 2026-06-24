package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ResumeLanguageRepository extends JpaRepository<ResumeLanguage, UUID> {
    List<ResumeLanguage> findAllByResumeIdAndResumeOwnerIdOrderBySortOrderAsc(UUID resumeId, UUID ownerId);
}
