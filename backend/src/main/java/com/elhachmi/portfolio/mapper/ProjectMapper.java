package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.ProjectRequest;
import com.elhachmi.portfolio.dto.response.ProjectResponse;
import com.elhachmi.portfolio.entity.Project;
import com.elhachmi.portfolio.entity.Skill;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project project) {
        if (project == null) {
            return null;
        }

        List<String> techStack = project.getTechStack().stream()
                .map(Skill::getName)
                .toList();

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getSlug(),
                project.getDescription(),
                project.getImageUrl(),
                project.getLiveUrl(),
                project.getGithubUrl(),
                project.getFeatured(),
                project.getSortOrder(),
                techStack
        );
    }

    public Project toEntity(ProjectRequest request, String generatedSlug) {
        if (request == null) {
            return null;
        }

        Project project = new Project();
        project.setSlug(generatedSlug);
        updateEntityFromRequest(request, project);
        return project;
    }

    public void updateEntityFromRequest(ProjectRequest request, Project project) {
        if (request == null || project == null) {
            return;
        }

        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setImageUrl(request.imageUrl());
        project.setLiveUrl(request.liveUrl());
        project.setGithubUrl(request.githubUrl());
        
        if (request.featured() != null) {
            project.setFeatured(request.featured());
        }
        
        if (request.sortOrder() != null) {
            project.setSortOrder(request.sortOrder());
        }
        
        // Tech stack handled separately in service layer
    }
}
