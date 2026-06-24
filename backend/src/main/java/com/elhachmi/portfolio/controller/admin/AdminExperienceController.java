package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.ExperienceRequest;
import com.elhachmi.portfolio.dto.response.ExperienceResponse;
import com.elhachmi.portfolio.config.AdminApi;
import com.elhachmi.portfolio.service.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/experiences")
@Tag(name = "Admin Experiences", description = "Manage work experience entries")
@AdminApi
public class AdminExperienceController {

    private final ExperienceService experienceService;

    public AdminExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    @Operation(summary = "List work experiences")
    public ResponseEntity<List<ExperienceResponse>> getAll() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a work experience")
    public ResponseEntity<ExperienceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getExperience(id));
    }

    @PostMapping
    @Operation(summary = "Create a work experience")
    public ResponseEntity<ExperienceResponse> create(@Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.createExperience(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a work experience")
    public ResponseEntity<ExperienceResponse> update(@PathVariable Long id, @Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.updateExperience(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a work experience")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
