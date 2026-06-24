package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resume_profile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeProfile {
    @Id
    @Column(name = "resume_id")
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "full_name", length = 255)
    private String fullName;
    @Column(length = 255)
    private String headline;
    @Column(length = 320)
    private String email;
    @Column(length = 50)
    private String phone;
    @Column(length = 255)
    private String location;
    @Column(name = "website_url", length = 1000)
    private String websiteUrl;
    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(name = "photo_url", length = 1000)
    private String photoUrl;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
