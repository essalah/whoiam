package com.elhachmi.portfolio.repository;

import com.elhachmi.portfolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOrderBySortOrderAsc();
    List<Project> findByFeaturedTrueOrderBySortOrderAsc();
    Optional<Project> findBySlug(String slug);
}
