package com.elhachmi.portfolio.repository;

import com.elhachmi.portfolio.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findAllByOrderBySortOrderAsc();
}
