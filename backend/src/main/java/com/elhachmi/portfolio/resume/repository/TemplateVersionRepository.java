package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.TemplateVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateVersionRepository extends JpaRepository<TemplateVersion, UUID> {
    Optional<TemplateVersion> findByTemplateKeyAndVersion(String templateKey, Integer version);
    Optional<TemplateVersion> findByTemplateKeyAndVersionAndActiveTrue(String templateKey, Integer version);
}
