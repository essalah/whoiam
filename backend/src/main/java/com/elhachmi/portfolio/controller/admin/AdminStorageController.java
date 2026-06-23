package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/storage")
@Tag(name = "Admin Storage", description = "Upload files to Cloudinary for portfolio assets")
@SecurityRequirement(name = "bearerAuth")
public class AdminStorageController {

    private final StorageService storageService;

    public AdminStorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file to Cloudinary")
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "portfolio") String folder) {
        String uploadedUrl = storageService.uploadFile(file, folder);
        return ResponseEntity.ok(uploadedUrl);
    }
}
