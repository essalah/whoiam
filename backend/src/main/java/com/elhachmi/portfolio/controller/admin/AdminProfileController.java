package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.dto.request.ProfileRequest;
import com.elhachmi.portfolio.dto.response.ProfileResponse;
import com.elhachmi.portfolio.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/profile")
@Tag(name = "Admin Profile", description = "Manage portfolio profile data and avatar uploads")
@SecurityRequirement(name = "bearerAuth")
public class AdminProfileController {

    private final ProfileService profileService;

    public AdminProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Get profile details")
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update profile details")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable Long id, @Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile avatar image")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(profileService.uploadAvatar(id, file));
    }
}
