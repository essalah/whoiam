package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.LanguageRequest;
import com.elhachmi.portfolio.dto.response.LanguageResponse;
import com.elhachmi.portfolio.service.LanguageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/languages")
@Tag(name = "Admin Languages", description = "Manage language proficiency entries")
@SecurityRequirement(name = "bearerAuth")
public class AdminLanguageController {

    private final LanguageService languageService;

    public AdminLanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAll() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.getLanguage(id));
    }

    @PostMapping
    public ResponseEntity<LanguageResponse> create(@Valid @RequestBody LanguageRequest request) {
        return ResponseEntity.ok(languageService.createLanguage(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageResponse> update(@PathVariable Long id, @Valid @RequestBody LanguageRequest request) {
        return ResponseEntity.ok(languageService.updateLanguage(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
