package com.elhachmi.portfolio.resume;

import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.identity.UserAccount;
import com.elhachmi.portfolio.identity.UserAccountRepository;
import com.elhachmi.portfolio.resume.dto.CreateResumeRequest;
import com.elhachmi.portfolio.resume.dto.ResumeSummaryResponse;
import com.elhachmi.portfolio.resume.entity.PlanEntitlement;
import com.elhachmi.portfolio.resume.entity.Resume;
import com.elhachmi.portfolio.resume.entity.ResumeProfile;
import com.elhachmi.portfolio.resume.enums.ResumePageSize;
import com.elhachmi.portfolio.resume.enums.ResumeStatus;
import com.elhachmi.portfolio.resume.repository.PlanEntitlementRepository;
import com.elhachmi.portfolio.resume.repository.ResumeProfileRepository;
import com.elhachmi.portfolio.resume.repository.ResumeRepository;
import com.elhachmi.portfolio.resume.repository.TemplateVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private static final String FREE_PLAN = "FREE";
    private static final String DEFAULT_TEMPLATE_KEY = "ats-classic-v1";
    private static final int DEFAULT_TEMPLATE_VERSION = 1;

    private final UserAccountRepository userAccountRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeProfileRepository resumeProfileRepository;
    private final PlanEntitlementRepository planEntitlementRepository;
    private final TemplateVersionRepository templateVersionRepository;

    @Transactional(readOnly = true)
    public List<ResumeSummaryResponse> list(Authentication authentication) {
        UserAccount owner = currentOwner(authentication);
        return resumeRepository.findAllByOwnerIdAndDeletedAtIsNullOrderByUpdatedAtDesc(owner.getId())
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResumeSummaryResponse get(UUID resumeId, Authentication authentication) {
        UserAccount owner = currentOwner(authentication);
        Resume resume = resumeRepository.findByIdAndOwnerIdAndDeletedAtIsNull(resumeId, owner.getId())
                .orElseThrow(this::resumeNotFound);
        return toSummary(resume);
    }

    @Transactional
    public ResumeSummaryResponse create(CreateResumeRequest request, Authentication authentication) {
        UserAccount owner = currentOwner(authentication);

        // V0 correctness first: the FREE plan row serializes the count-and-create
        // operation. Replace this global lock with an owner-scoped lock as the
        // product grows beyond low-volume first-resume creation.
        PlanEntitlement entitlement = planEntitlementRepository.findByPlanKeyForUpdate(FREE_PLAN)
                .orElseThrow(() -> new IllegalStateException("FREE plan entitlement is not configured"));

        long currentResumeCount = resumeRepository.countByOwnerIdAndDeletedAtIsNull(owner.getId());
        if (currentResumeCount >= entitlement.getMaxActiveResumes()) {
            throw new ResumeLimitReachedException("Your plan allows one active resume");
        }

        templateVersionRepository.findByTemplateKeyAndVersionAndActiveTrue(DEFAULT_TEMPLATE_KEY, DEFAULT_TEMPLATE_VERSION)
                .orElseThrow(() -> new IllegalStateException("Default resume template is not active"));

        Resume resume = Resume.builder()
                .owner(owner)
                .dashboardTitle(request.dashboardTitle().trim())
                .templateKey(DEFAULT_TEMPLATE_KEY)
                .templateVersion(DEFAULT_TEMPLATE_VERSION)
                .locale("en")
                .pageSize(ResumePageSize.A4)
                .status(ResumeStatus.DRAFT)
                .build();
        resume = resumeRepository.saveAndFlush(resume);

        ResumeProfile profile = ResumeProfile.builder()
                .resume(resume)
                .build();
        resumeProfileRepository.save(profile);

        return toSummary(resume);
    }

    private UserAccount currentOwner(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Account not found");
        }
        String normalizedEmail = UserAccount.normalizeEmail(authentication.getName());
        return userAccountRepository.findByNormalizedEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private ResumeSummaryResponse toSummary(Resume resume) {
        return new ResumeSummaryResponse(
                resume.getId(), resume.getDashboardTitle(), resume.getTemplateKey(),
                resume.getTemplateVersion(), resume.getLocale(), resume.getPageSize(),
                resume.getStatus(), resume.getLockVersion(), resume.getCreatedAt(), resume.getUpdatedAt()
        );
    }

    private ResourceNotFoundException resumeNotFound() {
        return new ResourceNotFoundException("Resume not found");
    }
}
