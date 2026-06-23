package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.EducationRequest;
import com.elhachmi.portfolio.dto.response.EducationResponse;
import com.elhachmi.portfolio.entity.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public EducationResponse toResponse(Education education) {
        if (education == null) {
            return null;
        }

        return new EducationResponse(
                education.getId(),
                education.getInstitution(),
                education.getDegree(),
                education.getField(),
                education.getStartDate(),
                education.getEndDate(),
                education.getDescription(),
                education.getSortOrder()
        );
    }

    public Education toEntity(EducationRequest request) {
        if (request == null) {
            return null;
        }

        Education education = new Education();
        updateEntityFromRequest(request, education);
        return education;
    }

    public void updateEntityFromRequest(EducationRequest request, Education education) {
        if (request == null || education == null) {
            return;
        }

        education.setInstitution(request.institution());
        education.setDegree(request.degree());
        education.setField(request.field());
        education.setStartDate(request.startDate());
        education.setEndDate(request.endDate());
        education.setDescription(request.description());
        
        if (request.sortOrder() != null) {
            education.setSortOrder(request.sortOrder());
        }
    }
}
