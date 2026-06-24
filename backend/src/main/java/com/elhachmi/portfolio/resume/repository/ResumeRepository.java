package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    Optional<Resume> findByIdAndOwnerIdAndDeletedAtIsNull(UUID id, UUID ownerId);
    long countByOwnerIdAndDeletedAtIsNull(UUID ownerId);
    List<Resume> findAllByOwnerIdAndDeletedAtIsNullOrderByUpdatedAtDesc(UUID ownerId);
}
