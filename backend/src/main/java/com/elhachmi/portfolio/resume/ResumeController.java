package com.elhachmi.portfolio.resume;

import com.elhachmi.portfolio.resume.dto.CreateResumeRequest;
import com.elhachmi.portfolio.resume.dto.ResumeSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping
    public List<ResumeSummaryResponse> list(Authentication authentication) {
        return resumeService.list(authentication);
    }

    @PostMapping
    public ResponseEntity<ResumeSummaryResponse> create(
            @Valid @RequestBody CreateResumeRequest request,
            Authentication authentication
    ) {
        ResumeSummaryResponse created = resumeService.create(request, authentication);
        return ResponseEntity.created(URI.create("/api/v1/resumes/" + created.id())).body(created);
    }

    @GetMapping("/{resumeId}")
    public ResumeSummaryResponse get(@PathVariable UUID resumeId, Authentication authentication) {
        return resumeService.get(resumeId, authentication);
    }
}
