package com.elhachmi.portfolio.controller.admin;

import com.elhachmi.portfolio.config.AdminApi;
import com.elhachmi.portfolio.exception.ApiError;
import com.elhachmi.portfolio.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@AdminApi
public class AdminStorageController {

    private final StorageService storageService;

    public AdminStorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a portfolio asset", description = "Uploads a multipart file to the requested Cloudinary folder and returns its public URL.")
    @ApiResponse(responseCode = "200", description = "File uploaded")
    @ApiResponse(responseCode = "502", description = "Cloud storage operation failed", content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "portfolio") String folder) {
        String uploadedUrl = storageService.uploadFile(file, folder);
        return ResponseEntity.ok(uploadedUrl);
    }
}
