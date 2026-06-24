package com.elhachmi.portfolio.controller.pub;

import com.elhachmi.portfolio.dto.response.CertificationResponse;
import com.elhachmi.portfolio.dto.response.EducationResponse;
import com.elhachmi.portfolio.dto.response.ExperienceResponse;
import com.elhachmi.portfolio.dto.response.LanguageResponse;
import com.elhachmi.portfolio.dto.response.ProfileResponse;
import com.elhachmi.portfolio.dto.response.ProjectResponse;
import com.elhachmi.portfolio.dto.response.SkillResponse;
import com.elhachmi.portfolio.service.CertificationService;
import com.elhachmi.portfolio.service.EducationService;
import com.elhachmi.portfolio.service.ExperienceService;
import com.elhachmi.portfolio.service.LanguageService;
import com.elhachmi.portfolio.service.ProfileService;
import com.elhachmi.portfolio.service.ProjectService;
import com.elhachmi.portfolio.service.SkillService;
import com.elhachmi.portfolio.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolio")
@Tag(name = "Public Portfolio", description = "Public portfolio data endpoints")
@ApiResponses({
        @ApiResponse(responseCode = "404", description = "Portfolio resource not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(schema = @Schema(implementation = ApiError.class)))
})
public class PortfolioController {

    private final ProfileService profileService;
    private final ExperienceService experienceService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final EducationService educationService;
    private final CertificationService certificationService;
    private final LanguageService languageService;

    public PortfolioController(ProfileService profileService,
                                ExperienceService experienceService,
                                ProjectService projectService,
                                SkillService skillService,
                                EducationService educationService,
                                CertificationService certificationService,
                                LanguageService languageService) {
        this.profileService = profileService;
        this.experienceService = experienceService;
        this.projectService = projectService;
        this.skillService = skillService;
        this.educationService = educationService;
        this.certificationService = certificationService;
        this.languageService = languageService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get the public profile")
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @GetMapping("/experiences")
    @Operation(summary = "List work experiences")
    public ResponseEntity<List<ExperienceResponse>> getExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/projects")
    @Operation(summary = "List projects")
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/projects/featured")
    @Operation(summary = "List featured projects")
    public ResponseEntity<List<ProjectResponse>> getFeaturedProjects() {
        return ResponseEntity.ok(projectService.getFeaturedProjects());
    }

    @GetMapping("/projects/{slug}")
    @Operation(summary = "Get a project by slug")
    public ResponseEntity<ProjectResponse> getProjectBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(projectService.getProjectBySlug(slug));
    }

    @GetMapping("/skills")
    @Operation(summary = "List skills")
    public ResponseEntity<List<SkillResponse>> getSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/education")
    @Operation(summary = "List education entries")
    public ResponseEntity<List<EducationResponse>> getEducation() {
        return ResponseEntity.ok(educationService.getAllEducation());
    }

    @GetMapping("/certifications")
    @Operation(summary = "List certifications")
    public ResponseEntity<List<CertificationResponse>> getCertifications() {
        return ResponseEntity.ok(certificationService.getAllCertifications());
    }

    @GetMapping("/languages")
    @Operation(summary = "List languages")
    public ResponseEntity<List<LanguageResponse>> getLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }
}
