package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.CertificationRequest;
import com.elhachmi.portfolio.dto.response.CertificationResponse;
import com.elhachmi.portfolio.config.AdminApi;
import com.elhachmi.portfolio.service.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/certifications")
@Tag(name = "Admin Certifications", description = "Manage certification entries")
@AdminApi
public class AdminCertificationController {

    private final CertificationService certificationService;

    public AdminCertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping
    @Operation(summary = "List certifications")
    public ResponseEntity<List<CertificationResponse>> getAll() {
        return ResponseEntity.ok(certificationService.getAllCertifications());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a certification")
    public ResponseEntity<CertificationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(certificationService.getCertification(id));
    }

    @PostMapping
    @Operation(summary = "Create a certification")
    public ResponseEntity<CertificationResponse> create(@Valid @RequestBody CertificationRequest request) {
        return ResponseEntity.ok(certificationService.createCertification(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a certification")
    public ResponseEntity<CertificationResponse> update(@PathVariable Long id, @Valid @RequestBody CertificationRequest request) {
        return ResponseEntity.ok(certificationService.updateCertification(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a certification")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}
