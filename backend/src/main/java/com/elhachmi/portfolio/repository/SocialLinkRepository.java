package com.elhachmi.portfolio.repository;

import com.elhachmi.portfolio.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    List<SocialLink> findByProfileId(Long profileId);
}
