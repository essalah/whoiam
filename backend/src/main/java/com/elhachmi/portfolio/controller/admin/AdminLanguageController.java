package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.LanguageRequest;
import com.elhachmi.portfolio.dto.response.LanguageResponse;
import com.elhachmi.portfolio.config.AdminApi;
import com.elhachmi.portfolio.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/languages")
@Tag(name = "Admin Languages", description = "Manage language proficiency entries")
@AdminApi
public class AdminLanguageController {

    private final LanguageService languageService;

    public AdminLanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    @Operation(summary = "List languages")
    public ResponseEntity<List<LanguageResponse>> getAll() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a language")
    public ResponseEntity<LanguageResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.getLanguage(id));
    }

    @PostMapping
    @Operation(summary = "Create a language")
    public ResponseEntity<LanguageResponse> create(@Valid @RequestBody LanguageRequest request) {
        return ResponseEntity.ok(languageService.createLanguage(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a language")
    public ResponseEntity<LanguageResponse> update(@PathVariable Long id, @Valid @RequestBody LanguageRequest request) {
        return ResponseEntity.ok(languageService.updateLanguage(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a language")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
