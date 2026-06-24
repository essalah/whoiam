package com.elhachmi.portfolio.resume;

import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.identity.UserAccount;
import com.elhachmi.portfolio.identity.UserAccountRepository;
import com.elhachmi.portfolio.resume.dto.CreateResumeRequest;
import com.elhachmi.portfolio.resume.dto.ResumeSummaryResponse;
import com.elhachmi.portfolio.resume.entity.PlanEntitlement;
import com.elhachmi.portfolio.resume.entity.Resume;
import com.elhachmi.portfolio.resume.entity.ResumeProfile;
import com.elhachmi.portfolio.resume.entity.TemplateVersion;
import com.elhachmi.portfolio.resume.enums.ResumePageSize;
import com.elhachmi.portfolio.resume.enums.ResumeStatus;
import com.elhachmi.portfolio.resume.repository.PlanEntitlementRepository;
import com.elhachmi.portfolio.resume.repository.ResumeProfileRepository;
import com.elhachmi.portfolio.resume.repository.ResumeRepository;
import com.elhachmi.portfolio.resume.repository.TemplateVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeServiceTest {
    private static final UUID OWNER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RESUME_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Mock private UserAccountRepository userAccountRepository;
    @Mock private ResumeRepository resumeRepository;
    @Mock private ResumeProfileRepository resumeProfileRepository;
    @Mock private PlanEntitlementRepository planEntitlementRepository;
    @Mock private TemplateVersionRepository templateVersionRepository;
    @Mock private Authentication authentication;

    private ResumeService resumeService;
    private UserAccount owner;

    @BeforeEach
    void setUp() {
        resumeService = new ResumeService(
                userAccountRepository,
                resumeRepository,
                resumeProfileRepository,
                planEntitlementRepository,
                templateVersionRepository
        );
        owner = UserAccount.builder()
                .id(OWNER_ID)
                .email("owner@example.com")
                .normalizedEmail("owner@example.com")
                .passwordHash("hash")
                .build();
    }

    @Test
    void createUsesFreeDefaultsAndSavesEmptyProfile() {
        authenticateAsOwner();
        when(planEntitlementRepository.findByPlanKeyForUpdate("FREE"))
                .thenReturn(Optional.of(freeEntitlement()));
        when(resumeRepository.countByOwnerIdAndDeletedAtIsNull(OWNER_ID)).thenReturn(0L);
        when(templateVersionRepository.findByTemplateKeyAndVersionAndActiveTrue("ats-classic-v1", 1))
                .thenReturn(Optional.of(defaultTemplate()));
        when(resumeRepository.saveAndFlush(any(Resume.class))).thenAnswer(invocation -> {
            Resume saved = invocation.getArgument(0);
            saved.setId(RESUME_ID);
            saved.setCreatedAt(Instant.parse("2026-06-24T08:00:00Z"));
            saved.setUpdatedAt(Instant.parse("2026-06-24T08:00:00Z"));
            return saved;
        });

        ResumeSummaryResponse response = resumeService.create(new CreateResumeRequest("  First resume  "), authentication);

        ArgumentCaptor<Resume> resumeCaptor = ArgumentCaptor.forClass(Resume.class);
        verify(resumeRepository).saveAndFlush(resumeCaptor.capture());
        Resume created = resumeCaptor.getValue();
        assertThat(created.getOwner()).isSameAs(owner);
        assertThat(created.getDashboardTitle()).isEqualTo("First resume");
        assertThat(created.getTemplateKey()).isEqualTo("ats-classic-v1");
        assertThat(created.getTemplateVersion()).isEqualTo(1);
        assertThat(created.getLocale()).isEqualTo("en");
        assertThat(created.getPageSize()).isEqualTo(ResumePageSize.A4);
        assertThat(created.getStatus()).isEqualTo(ResumeStatus.DRAFT);

        ArgumentCaptor<ResumeProfile> profileCaptor = ArgumentCaptor.forClass(ResumeProfile.class);
        verify(resumeProfileRepository).save(profileCaptor.capture());
        assertThat(profileCaptor.getValue().getResume()).isSameAs(created);
        assertThat(profileCaptor.getValue().getFullName()).isNull();
        assertThat(response.id()).isEqualTo(RESUME_ID);
        assertThat(response.dashboardTitle()).isEqualTo("First resume");
    }

    @Test
    void createRejectsSecondActiveResume() {
        authenticateAsOwner();
        when(planEntitlementRepository.findByPlanKeyForUpdate("FREE"))
                .thenReturn(Optional.of(freeEntitlement()));
        when(resumeRepository.countByOwnerIdAndDeletedAtIsNull(OWNER_ID)).thenReturn(1L);

        assertThatThrownBy(() -> resumeService.create(new CreateResumeRequest("Second"), authentication))
                .isInstanceOf(ResumeLimitReachedException.class)
                .hasMessage("Your plan allows one active resume");

        verifyNoInteractions(templateVersionRepository, resumeProfileRepository);
        verify(resumeRepository, never()).saveAndFlush(any());
    }

    @Test
    void createFailsWhenFreeEntitlementIsMissing() {
        authenticateAsOwner();
        when(planEntitlementRepository.findByPlanKeyForUpdate("FREE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resumeService.create(new CreateResumeRequest("Resume"), authentication))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("FREE plan entitlement is not configured");

        verify(resumeRepository, never()).countByOwnerIdAndDeletedAtIsNull(any());
        verifyNoInteractions(templateVersionRepository, resumeProfileRepository);
    }

    @Test
    void createFailsWhenDefaultTemplateIsMissingOrInactive() {
        authenticateAsOwner();
        when(planEntitlementRepository.findByPlanKeyForUpdate("FREE"))
                .thenReturn(Optional.of(freeEntitlement()));
        when(resumeRepository.countByOwnerIdAndDeletedAtIsNull(OWNER_ID)).thenReturn(0L);
        when(templateVersionRepository.findByTemplateKeyAndVersionAndActiveTrue("ats-classic-v1", 1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> resumeService.create(new CreateResumeRequest("Resume"), authentication))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Default resume template is not active");

        verify(resumeRepository, never()).saveAndFlush(any());
        verifyNoInteractions(resumeProfileRepository);
    }

    @Test
    void getUsesOwnerScopedLookup() {
        authenticateAsOwner();
        Resume resume = resume("Owned resume");
        when(resumeRepository.findByIdAndOwnerIdAndDeletedAtIsNull(RESUME_ID, OWNER_ID))
                .thenReturn(Optional.of(resume));

        ResumeSummaryResponse response = resumeService.get(RESUME_ID, authentication);

        assertThat(response.id()).isEqualTo(RESUME_ID);
        assertThat(response.dashboardTitle()).isEqualTo("Owned resume");
        verify(resumeRepository).findByIdAndOwnerIdAndDeletedAtIsNull(RESUME_ID, OWNER_ID);
        verify(resumeRepository, never()).findById(any());
    }

    @Test
    void listUsesOwnerScopeAndPreservesRepositoryOrder() {
        authenticateAsOwner();
        Resume first = resume("Recently updated");
        Resume second = resume("Older");
        second.setId(UUID.fromString("33333333-3333-3333-3333-333333333333"));
        when(resumeRepository.findAllByOwnerIdAndDeletedAtIsNullOrderByUpdatedAtDesc(OWNER_ID))
                .thenReturn(List.of(first, second));

        List<ResumeSummaryResponse> responses = resumeService.list(authentication);

        assertThat(responses).extracting(ResumeSummaryResponse::dashboardTitle)
                .containsExactly("Recently updated", "Older");
        verify(resumeRepository).findAllByOwnerIdAndDeletedAtIsNullOrderByUpdatedAtDesc(OWNER_ID);
        verify(resumeRepository, never()).findAll();
    }

    @Test
    void crossOwnerResumeIdReturnsGenericNotFound() {
        authenticateAsOwner();
        when(resumeRepository.findByIdAndOwnerIdAndDeletedAtIsNull(RESUME_ID, OWNER_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> resumeService.get(RESUME_ID, authentication))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Resume not found");

        verify(resumeRepository).findByIdAndOwnerIdAndDeletedAtIsNull(RESUME_ID, OWNER_ID);
        verify(resumeRepository, never()).findById(any());
    }

    private void authenticateAsOwner() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("  OWNER@EXAMPLE.COM ");
        when(userAccountRepository.findByNormalizedEmail("owner@example.com")).thenReturn(Optional.of(owner));
    }

    private PlanEntitlement freeEntitlement() {
        return PlanEntitlement.builder()
                .planKey("FREE")
                .maxActiveResumes(1)
                .allowedTemplateKeys("[\"ats-classic-v1\"]")
                .build();
    }

    private TemplateVersion defaultTemplate() {
        return TemplateVersion.builder()
                .templateKey("ats-classic-v1")
                .version(1)
                .displayName("ATS Classic")
                .active(true)
                .build();
    }

    private Resume resume(String title) {
        return Resume.builder()
                .id(RESUME_ID)
                .owner(owner)
                .dashboardTitle(title)
                .createdAt(Instant.parse("2026-06-24T08:00:00Z"))
                .updatedAt(Instant.parse("2026-06-24T08:00:00Z"))
                .build();
    }
}
