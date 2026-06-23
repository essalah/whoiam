package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.ExperienceRequest;
import com.elhachmi.portfolio.dto.response.ExperienceResponse;
import com.elhachmi.portfolio.entity.Achievement;
import com.elhachmi.portfolio.entity.Experience;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.ExperienceMapper;
import com.elhachmi.portfolio.repository.ExperienceRepository;
import com.elhachmi.portfolio.service.ExperienceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public List<ExperienceResponse> getAllExperiences() {
        return experienceRepository.findAllByOrderBySortOrderAsc().stream()
                .map(experienceMapper::toResponse)
                .toList();
    }

    @Override
    public ExperienceResponse getExperience(Long id) {
        Experience experience = findExperienceById(id);
        return experienceMapper.toResponse(experience);
    }

    @Override
    @Transactional
    public ExperienceResponse createExperience(ExperienceRequest request) {
        Experience experience = experienceMapper.toEntity(request);
        setAchievements(experience, request.achievements());
        experience = experienceRepository.save(experience);
        return experienceMapper.toResponse(experience);
    }

    @Override
    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequest request) {
        Experience experience = findExperienceById(id);
        experienceMapper.updateEntityFromRequest(request, experience);
        setAchievements(experience, request.achievements());
        return experienceMapper.toResponse(experienceRepository.save(experience));
    }

    @Override
    @Transactional
    public void deleteExperience(Long id) {
        Experience experience = findExperienceById(id);
        experienceRepository.delete(experience);
    }

    private Experience findExperienceById(Long id) {
        return experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
    }

    private void setAchievements(Experience experience, List<String> achievements) {
        experience.getAchievements().clear();
        if (achievements == null || achievements.isEmpty()) {
            return;
        }

        achievements.stream()
                .filter(StringUtils::hasText)
                .map(description -> Achievement.builder()
                        .description(description)
                        .experience(experience)
                        .build())
                .forEach(experience.getAchievements()::add);
    }
}
