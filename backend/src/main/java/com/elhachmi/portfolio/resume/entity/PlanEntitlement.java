package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "plan_entitlement", uniqueConstraints = @UniqueConstraint(name = "uk_plan_entitlement_key", columnNames = "plan_key"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanEntitlement {
    @Id
    @Column(name = "plan_key", nullable = false, length = 50)
    private String planKey;
    @Column(name = "max_active_resumes", nullable = false)
    private Integer maxActiveResumes;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "allowed_template_keys", nullable = false)
    @Builder.Default
    private String allowedTemplateKeys = "[]";
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
