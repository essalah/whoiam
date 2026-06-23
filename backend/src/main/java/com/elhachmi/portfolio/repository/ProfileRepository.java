package com.elhachmi.portfolio.repository;

import com.elhachmi.portfolio.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Returns the first (and typically only) profile.
     * This is a single-user portfolio system.
     */
    Optional<Profile> findFirstByOrderByIdAsc();
}
