package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.ExperienceRequest;
import com.elhachmi.portfolio.dto.response.ExperienceResponse;
import com.elhachmi.portfolio.service.ExperienceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/experiences")
@Tag(name = "Admin Experiences", description = "Manage work experience entries")
@SecurityRequirement(name = "bearerAuth")
public class AdminExperienceController {

    private final ExperienceService experienceService;

    public AdminExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAll() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getExperience(id));
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> create(@Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.createExperience(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> update(@PathVariable Long id, @Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.updateExperience(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
