package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.ProjectRequest;
import com.elhachmi.portfolio.dto.response.ProjectResponse;
import com.elhachmi.portfolio.entity.Project;
import com.elhachmi.portfolio.entity.Skill;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.ProjectMapper;
import com.elhachmi.portfolio.repository.ProjectRepository;
import com.elhachmi.portfolio.repository.SkillRepository;
import com.elhachmi.portfolio.service.ProjectService;
import com.elhachmi.portfolio.storage.StorageService;
import com.elhachmi.portfolio.util.SlugUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ProjectMapper projectMapper;
    private final StorageService storageService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              SkillRepository skillRepository,
                              ProjectMapper projectMapper,
                              StorageService storageService) {
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.projectMapper = projectMapper;
        this.storageService = storageService;
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAllByOrderBySortOrderAsc().stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProjectResponse> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrueOrderBySortOrderAsc().stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse getProjectBySlug(String slug) {
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with slug: " + slug));
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        String slug = generateUniqueSlug(request.title());
        Project project = projectMapper.toEntity(request, slug);
        setTechStack(project, request.techStackIds());
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getTitle().equals(request.title())) {
            project.setSlug(generateUniqueSlug(request.title()));
        }

        projectMapper.updateEntityFromRequest(request, project);
        setTechStack(project, request.techStackIds());
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public String uploadProjectImage(Long id, MultipartFile file) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        String imageUrl = storageService.uploadFile(file, "project-images");
        project.setImageUrl(imageUrl);
        projectRepository.save(project);
        return imageUrl;
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = SlugUtils.toSlug(title);
        String candidate = baseSlug;
        int suffix = 1;
        while (projectRepository.findBySlug(candidate).isPresent()) {
            candidate = baseSlug + "-" + suffix++;
        }
        return candidate;
    }

    private void setTechStack(Project project, List<Long> techStackIds) {
        Set<Skill> skills = new HashSet<>();
        if (techStackIds != null && !techStackIds.isEmpty()) {
            List<Skill> foundSkills = skillRepository.findAllById(techStackIds);
            if (foundSkills.size() != techStackIds.size()) {
                throw new ResourceNotFoundException("One or more skills were not found for the project tech stack.");
            }
            skills.addAll(foundSkills);
        }
        project.setTechStack(skills);
    }
}
