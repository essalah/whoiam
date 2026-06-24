package com.elhachmi.portfolio.resume.entity;

import com.elhachmi.portfolio.identity.UserAccount;
import com.elhachmi.portfolio.resume.enums.ResumePageSize;
import com.elhachmi.portfolio.resume.enums.ResumeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resume", indexes = @Index(name = "idx_resume_owner_active", columnList = "owner_id, deleted_at"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserAccount owner;

    @Column(name = "dashboard_title", nullable = false, length = 255)
    private String dashboardTitle;

    @Column(name = "template_key", nullable = false, length = 100)
    @Builder.Default
    private String templateKey = "ats-classic-v1";

    @Column(name = "template_version", nullable = false)
    @Builder.Default
    private Integer templateVersion = 1;

    @Column(nullable = false, length = 35)
    @Builder.Default
    private String locale = "en";

    @Enumerated(EnumType.STRING)
    @Column(name = "page_size", nullable = false, length = 20)
    @Builder.Default
    private ResumePageSize pageSize = ResumePageSize.A4;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Builder.Default
    private ResumeStatus status = ResumeStatus.DRAFT;

    @Version
    @Column(name = "lock_version", nullable = false)
    @Builder.Default
    private Long lockVersion = 0L;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
