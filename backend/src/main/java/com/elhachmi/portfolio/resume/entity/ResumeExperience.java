package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_experience", indexes = @Index(name = "idx_resume_experience_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeExperience extends ResumeItem {
    @Column(length = 255)
    private String employer;
    @Column(length = 255)
    private String position;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "current_position", nullable = false)
    @Builder.Default
    private boolean currentPosition = false;
    @Column(length = 255)
    private String location;
    @Column(columnDefinition = "TEXT")
    private String description;
}
