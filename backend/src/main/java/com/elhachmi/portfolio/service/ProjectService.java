package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.ProjectRequest;
import com.elhachmi.portfolio.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    List<ProjectResponse> getAllProjects();

    List<ProjectResponse> getFeaturedProjects();

    ProjectResponse getProject(Long id);

    ProjectResponse getProjectBySlug(String slug);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);

    String uploadProjectImage(Long id, org.springframework.web.multipart.MultipartFile file);
}
