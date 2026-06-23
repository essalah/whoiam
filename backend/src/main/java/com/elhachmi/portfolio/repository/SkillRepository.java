package com.elhachmi.portfolio.repository;

import com.elhachmi.portfolio.entity.Skill;
import com.elhachmi.portfolio.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByOrderBySortOrderAsc();
    List<Skill> findByCategoryOrderBySortOrderAsc(SkillCategory category);
}
