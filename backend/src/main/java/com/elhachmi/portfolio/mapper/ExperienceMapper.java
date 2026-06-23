package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.ExperienceRequest;
import com.elhachmi.portfolio.dto.response.ExperienceResponse;
import com.elhachmi.portfolio.entity.Achievement;
import com.elhachmi.portfolio.entity.Experience;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperienceMapper {

    public ExperienceResponse toResponse(Experience experience) {
        if (experience == null) {
            return null;
        }

        List<String> achievements = experience.getAchievements().stream()
                .map(Achievement::getDescription)
                .toList();

        return new ExperienceResponse(
                experience.getId(),
                experience.getCompany(),
                experience.getRole(),
                experience.getStartDate(),
                experience.getEndDate(),
                experience.getLocation(),
                experience.getDescription(),
                experience.getSortOrder(),
                achievements
        );
    }

    public Experience toEntity(ExperienceRequest request) {
        if (request == null) {
            return null;
        }

        Experience experience = new Experience();
        updateEntityFromRequest(request, experience);
        return experience;
    }

    public void updateEntityFromRequest(ExperienceRequest request, Experience experience) {
        if (request == null || experience == null) {
            return;
        }

        experience.setCompany(request.company());
        experience.setRole(request.role());
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.endDate());
        experience.setLocation(request.location());
        experience.setDescription(request.description());
        
        if (request.sortOrder() != null) {
            experience.setSortOrder(request.sortOrder());
        }

        // Achievements handled separately in service layer
    }
}
