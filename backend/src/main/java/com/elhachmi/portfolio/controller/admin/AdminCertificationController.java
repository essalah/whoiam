package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.CertificationRequest;
import com.elhachmi.portfolio.dto.response.CertificationResponse;
import com.elhachmi.portfolio.service.CertificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/certifications")
@Tag(name = "Admin Certifications", description = "Manage certification entries")
@SecurityRequirement(name = "bearerAuth")
public class AdminCertificationController {

    private final CertificationService certificationService;

    public AdminCertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping
    public ResponseEntity<List<CertificationResponse>> getAll() {
        return ResponseEntity.ok(certificationService.getAllCertifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(certificationService.getCertification(id));
    }

    @PostMapping
    public ResponseEntity<CertificationResponse> create(@Valid @RequestBody CertificationRequest request) {
        return ResponseEntity.ok(certificationService.createCertification(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificationResponse> update(@PathVariable Long id, @Valid @RequestBody CertificationRequest request) {
        return ResponseEntity.ok(certificationService.updateCertification(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}
