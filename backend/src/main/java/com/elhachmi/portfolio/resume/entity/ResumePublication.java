package com.elhachmi.portfolio.resume.entity;

import com.elhachmi.portfolio.resume.enums.PublicationState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resume_publication", uniqueConstraints = {
        @UniqueConstraint(name = "uk_publication_resume", columnNames = "resume_id"),
        @UniqueConstraint(name = "uk_publication_slug", columnNames = "public_slug")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumePublication {
    @Id
    @Column(name = "resume_id")
    private UUID id;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id")
    private Resume resume;
    @Column(name = "public_slug", nullable = false, length = 255)
    private String publicSlug;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Builder.Default
    private PublicationState state = PublicationState.UNPUBLISHED;
    @Column(name = "published_at")
    private Instant publishedAt;
    @Column(name = "revoked_at")
    private Instant revokedAt;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
