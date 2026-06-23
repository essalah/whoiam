package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.EducationRequest;
import com.elhachmi.portfolio.dto.response.EducationResponse;

import java.util.List;

public interface EducationService {

    List<EducationResponse> getAllEducation();

    EducationResponse getEducation(Long id);

    EducationResponse createEducation(EducationRequest request);

    EducationResponse updateEducation(Long id, EducationRequest request);

    void deleteEducation(Long id);
}
