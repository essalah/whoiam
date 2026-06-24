package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ResumeProjectRepository extends JpaRepository<ResumeProject, UUID> {
    List<ResumeProject> findAllByResumeIdAndResumeOwnerIdOrderBySortOrderAsc(UUID resumeId, UUID ownerId);
}
