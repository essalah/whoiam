package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.ResumeLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ResumeLinkRepository extends JpaRepository<ResumeLink, UUID> {
    List<ResumeLink> findAllByResumeIdAndResumeOwnerIdOrderBySortOrderAsc(UUID resumeId, UUID ownerId);
}
