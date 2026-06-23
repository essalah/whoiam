package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.SkillRequest;
import com.elhachmi.portfolio.dto.response.SkillResponse;

import java.util.List;

public interface SkillService {

    List<SkillResponse> getAllSkills();

    SkillResponse getSkill(Long id);

    SkillResponse createSkill(SkillRequest request);

    SkillResponse updateSkill(Long id, SkillRequest request);

    void deleteSkill(Long id);
}
