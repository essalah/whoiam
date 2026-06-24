package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume_language", indexes = @Index(name = "idx_resume_language_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeLanguage extends ResumeItem {
    @Column(nullable = false, length = 255)
    private String name;
    @Column(length = 100)
    private String proficiency;
}
