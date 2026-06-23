package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.SkillRequest;
import com.elhachmi.portfolio.dto.response.SkillResponse;
import com.elhachmi.portfolio.entity.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillResponse toResponse(Skill skill) {
        if (skill == null) {
            return null;
        }

        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory().name(),
                skill.getProficiency().name(),
                skill.getSortOrder()
        );
    }

    public Skill toEntity(SkillRequest request) {
        if (request == null) {
            return null;
        }

        Skill skill = new Skill();
        updateEntityFromRequest(request, skill);
        return skill;
    }

    public void updateEntityFromRequest(SkillRequest request, Skill skill) {
        if (request == null || skill == null) {
            return;
        }

        skill.setName(request.name());
        skill.setCategory(request.category());
        skill.setProficiency(request.proficiency());

        if (request.sortOrder() != null) {
            skill.setSortOrder(request.sortOrder());
        }
    }
}
