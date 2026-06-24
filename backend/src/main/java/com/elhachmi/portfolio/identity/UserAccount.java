package com.elhachmi.portfolio.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    public static final String DEFAULT_ROLE = "ROLE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(name = "normalized_email", nullable = false, unique = true, length = 320)
    private String normalizedEmail;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Builder.Default
    private UserAccountStatus status = UserAccountStatus.ACTIVE;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String role = DEFAULT_ROLE;

    @Column(name = "terms_version", length = 50)
    private String termsVersion;

    @Column(name = "terms_accepted_at")
    private Instant termsAcceptedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void setEmail(String email) {
        this.email = canonicalizeEmail(email);
        this.normalizedEmail = normalizeEmail(email);
    }

    public void setNormalizedEmail(String email) {
        this.normalizedEmail = normalizeEmail(email);
    }

    public boolean isActive() {
        return status == UserAccountStatus.ACTIVE && deletedAt == null;
    }

    public void disable() {
        status = UserAccountStatus.DISABLED;
    }

    public void markDeleted(Instant deletedAt) {
        status = UserAccountStatus.DELETED;
        this.deletedAt = deletedAt;
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private static String canonicalizeEmail(String email) {
        return email == null ? null : email.trim();
    }

    @PrePersist
    @PreUpdate
    private void normalizeEmailBeforeWrite() {
        email = canonicalizeEmail(email);
        normalizedEmail = normalizeEmail(email);
    }
}
