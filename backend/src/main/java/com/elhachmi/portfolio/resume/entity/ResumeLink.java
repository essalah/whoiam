package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume_link", indexes = @Index(name = "idx_resume_link_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeLink extends ResumeItem {
    @Column(length = 100)
    private String label;
    @Column(nullable = false, length = 1000)
    private String url;
    @Column(name = "link_type", length = 50)
    private String linkType;
}
