package com.elhachmi.portfolio.entity;

import com.elhachmi.portfolio.entity.enums.SocialPlatform;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialPlatform platform;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
