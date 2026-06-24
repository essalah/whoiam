package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.EducationRequest;
import com.elhachmi.portfolio.dto.response.EducationResponse;
import com.elhachmi.portfolio.config.AdminApi;
import com.elhachmi.portfolio.service.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/education")
@Tag(name = "Admin Education", description = "Manage education entries")
@AdminApi
public class AdminEducationController {

    private final EducationService educationService;

    public AdminEducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    @Operation(summary = "List education entries")
    public ResponseEntity<List<EducationResponse>> getAll() {
        return ResponseEntity.ok(educationService.getAllEducation());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an education entry")
    public ResponseEntity<EducationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(educationService.getEducation(id));
    }

    @PostMapping
    @Operation(summary = "Create an education entry")
    public ResponseEntity<EducationResponse> create(@Valid @RequestBody EducationRequest request) {
        return ResponseEntity.ok(educationService.createEducation(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an education entry")
    public ResponseEntity<EducationResponse> update(@PathVariable Long id, @Valid @RequestBody EducationRequest request) {
        return ResponseEntity.ok(educationService.updateEducation(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an education entry")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}
