package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.SkillRequest;
import com.elhachmi.portfolio.dto.response.SkillResponse;
import com.elhachmi.portfolio.entity.Skill;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.SkillMapper;
import com.elhachmi.portfolio.repository.SkillRepository;
import com.elhachmi.portfolio.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAllByOrderBySortOrderAsc().stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    @Override
    public SkillResponse getSkill(Long id) {
        return skillMapper.toResponse(findSkillById(id));
    }

    @Override
    @Transactional
    public SkillResponse createSkill(SkillRequest request) {
        Skill skill = skillMapper.toEntity(request);
        skill = skillRepository.save(skill);
        return skillMapper.toResponse(skill);
    }

    @Override
    @Transactional
    public SkillResponse updateSkill(Long id, SkillRequest request) {
        Skill skill = findSkillById(id);
        skillMapper.updateEntityFromRequest(request, skill);
        return skillMapper.toResponse(skillRepository.save(skill));
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        Skill skill = findSkillById(id);
        skillRepository.delete(skill);
    }

    private Skill findSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
    }
}
