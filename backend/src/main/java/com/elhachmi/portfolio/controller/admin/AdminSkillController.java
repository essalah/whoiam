package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.SkillRequest;
import com.elhachmi.portfolio.dto.response.SkillResponse;
import com.elhachmi.portfolio.service.SkillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/skills")
@Tag(name = "Admin Skills", description = "Manage skill entries and categories")
@SecurityRequirement(name = "bearerAuth")
public class AdminSkillController {

    private final SkillService skillService;

    public AdminSkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAll() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkill(id));
    }

    @PostMapping
    public ResponseEntity<SkillResponse> create(@Valid @RequestBody SkillRequest request) {
        return ResponseEntity.ok(skillService.createSkill(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> update(@PathVariable Long id, @Valid @RequestBody SkillRequest request) {
        return ResponseEntity.ok(skillService.updateSkill(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
