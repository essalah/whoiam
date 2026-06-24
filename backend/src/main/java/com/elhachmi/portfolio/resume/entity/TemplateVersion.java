package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "template_version", uniqueConstraints = @UniqueConstraint(name = "uk_template_key_version", columnNames = {"template_key", "version"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TemplateVersion {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "template_key", nullable = false, length = 100)
    private String templateKey;
    @Column(nullable = false)
    private Integer version;
    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ats_metadata", nullable = false)
    @Builder.Default
    private String atsMetadata = "{}";
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
