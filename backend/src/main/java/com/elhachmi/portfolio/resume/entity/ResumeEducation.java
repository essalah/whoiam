package com.elhachmi.portfolio.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_education", indexes = @Index(name = "idx_resume_education_order", columnList = "resume_id, sort_order"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeEducation extends ResumeItem {
    @Column(length = 255)
    private String institution;
    @Column(length = 255)
    private String degree;
    @Column(name = "field_of_study", length = 255)
    private String fieldOfStudy;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "current_study", nullable = false)
    @Builder.Default
    private boolean currentStudy = false;
    @Column(length = 255)
    private String location;
    @Column(columnDefinition = "TEXT")
    private String description;
}
