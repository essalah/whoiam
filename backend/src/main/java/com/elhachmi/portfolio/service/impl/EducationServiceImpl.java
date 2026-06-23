package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.EducationRequest;
import com.elhachmi.portfolio.dto.response.EducationResponse;
import com.elhachmi.portfolio.entity.Education;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.EducationMapper;
import com.elhachmi.portfolio.repository.EducationRepository;
import com.elhachmi.portfolio.service.EducationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    public EducationServiceImpl(EducationRepository educationRepository, EducationMapper educationMapper) {
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Override
    public List<EducationResponse> getAllEducation() {
        return educationRepository.findAllByOrderBySortOrderAsc().stream()
                .map(educationMapper::toResponse)
                .toList();
    }

    @Override
    public EducationResponse getEducation(Long id) {
        return educationMapper.toResponse(findEducationById(id));
    }

    @Override
    @Transactional
    public EducationResponse createEducation(EducationRequest request) {
        Education education = educationMapper.toEntity(request);
        education = educationRepository.save(education);
        return educationMapper.toResponse(education);
    }

    @Override
    @Transactional
    public EducationResponse updateEducation(Long id, EducationRequest request) {
        Education education = findEducationById(id);
        educationMapper.updateEntityFromRequest(request, education);
        return educationMapper.toResponse(educationRepository.save(education));
    }

    @Override
    @Transactional
    public void deleteEducation(Long id) {
        educationRepository.delete(findEducationById(id));
    }

    private Education findEducationById(Long id) {
        return educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
    }
}
