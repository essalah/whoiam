package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resume_achievement", indexes = @Index(name = "idx_resume_achievement_order", columnList = "resume_id, experience_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeAchievement {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "experience_id", nullable = false)
    private ResumeExperience experience;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    private void validateOwnership() {
        Resume experienceResume = experience == null ? null : experience.getResume();
        boolean samePersistentResume = resume != null && experienceResume != null
                && resume.getId() != null && resume.getId().equals(experienceResume.getId());
        if (resume == null || experienceResume == null
                || (resume != experienceResume && !samePersistentResume)) {
            throw new IllegalStateException("Achievement and experience must belong to the same resume");
        }
    }
}
