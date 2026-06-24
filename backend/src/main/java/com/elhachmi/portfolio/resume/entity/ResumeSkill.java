package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume_skill", indexes = @Index(name = "idx_resume_skill_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeSkill extends ResumeItem {
    @Column(nullable = false, length = 255)
    private String name;
    @Column(length = 100)
    private String category;
    @Column(length = 100)
    private String proficiency;
}
