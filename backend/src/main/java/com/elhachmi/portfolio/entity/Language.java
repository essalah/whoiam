package com.elhachmi.portfolio.entity;

import com.elhachmi.portfolio.entity.enums.LanguageProficiency;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageProficiency proficiency;
}
