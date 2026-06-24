package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_project", indexes = @Index(name = "idx_resume_project_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeProject extends ResumeItem {
    @Column(nullable = false, length = 255)
    private String name;
    @Column(length = 255)
    private String role;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "project_url", length = 1000)
    private String projectUrl;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
}
