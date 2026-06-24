package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.SocialLinkRequest;
import com.elhachmi.portfolio.dto.response.SocialLinkResponse;
import com.elhachmi.portfolio.config.AdminApi;
import com.elhachmi.portfolio.service.SocialLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/profiles/{profileId}/social-links")
@Tag(name = "Admin Social Links", description = "Manage profile social links")
@AdminApi
public class AdminSocialLinkController {

    private final SocialLinkService socialLinkService;

    public AdminSocialLinkController(SocialLinkService socialLinkService) {
        this.socialLinkService = socialLinkService;
    }

    @GetMapping
    @Operation(summary = "List profile social links")
    public ResponseEntity<List<SocialLinkResponse>> getAll(@PathVariable Long profileId) {
        return ResponseEntity.ok(socialLinkService.getAllLinks(profileId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a profile social link")
    public ResponseEntity<SocialLinkResponse> getById(@PathVariable Long profileId, @PathVariable Long id) {
        return ResponseEntity.ok(socialLinkService.getLink(id));
    }

    @PostMapping
    @Operation(summary = "Create a profile social link")
    public ResponseEntity<SocialLinkResponse> create(@PathVariable Long profileId, @Valid @RequestBody SocialLinkRequest request) {
        return ResponseEntity.ok(socialLinkService.createLink(profileId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a profile social link")
    public ResponseEntity<SocialLinkResponse> update(@PathVariable Long profileId, @PathVariable Long id, @Valid @RequestBody SocialLinkRequest request) {
        return ResponseEntity.ok(socialLinkService.updateLink(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a profile social link")
    public ResponseEntity<Void> delete(@PathVariable Long profileId, @PathVariable Long id) {
        socialLinkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }
}
