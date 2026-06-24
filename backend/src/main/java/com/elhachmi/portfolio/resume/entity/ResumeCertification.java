package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_certification", indexes = @Index(name = "idx_resume_certification_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeCertification extends ResumeItem {
    @Column(nullable = false, length = 255)
    private String name;
    @Column(length = 255)
    private String issuer;
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Column(name = "credential_id", length = 255)
    private String credentialId;
    @Column(name = "credential_url", length = 1000)
    private String credentialUrl;
}
