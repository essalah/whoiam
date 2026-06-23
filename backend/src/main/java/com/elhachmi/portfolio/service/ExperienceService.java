package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.ExperienceRequest;
import com.elhachmi.portfolio.dto.response.ExperienceResponse;

import java.util.List;

public interface ExperienceService {

    List<ExperienceResponse> getAllExperiences();

    ExperienceResponse getExperience(Long id);

    ExperienceResponse createExperience(ExperienceRequest request);

    ExperienceResponse updateExperience(Long id, ExperienceRequest request);

    void deleteExperience(Long id);
}
