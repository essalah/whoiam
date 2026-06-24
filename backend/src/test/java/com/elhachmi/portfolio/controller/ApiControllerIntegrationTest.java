package com.elhachmi.portfolio.controller;

import com.elhachmi.portfolio.config.OpenApiConfig;
import com.elhachmi.portfolio.controller.admin.AdminSkillController;
import com.elhachmi.portfolio.controller.admin.AdminStorageController;
import com.elhachmi.portfolio.controller.admin.AuthController;
import com.elhachmi.portfolio.controller.pub.PortfolioController;
import com.elhachmi.portfolio.dto.response.AuthResponse;
import com.elhachmi.portfolio.dto.response.SkillResponse;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.exception.RestExceptionHandler;
import com.elhachmi.portfolio.exception.StorageException;
import com.elhachmi.portfolio.security.AdminUserDetailsService;
import com.elhachmi.portfolio.security.JwtAuthenticationFilter;
import com.elhachmi.portfolio.security.JwtTokenProvider;
import com.elhachmi.portfolio.security.SecurityConfig;
import com.elhachmi.portfolio.service.AuthService;
import com.elhachmi.portfolio.service.CertificationService;
import com.elhachmi.portfolio.service.EducationService;
import com.elhachmi.portfolio.service.ExperienceService;
import com.elhachmi.portfolio.service.LanguageService;
import com.elhachmi.portfolio.service.ProfileService;
import com.elhachmi.portfolio.service.ProjectService;
import com.elhachmi.portfolio.service.SkillService;
import com.elhachmi.portfolio.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PortfolioController.class, AuthController.class, AdminSkillController.class, AdminStorageController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtTokenProvider.class, RestExceptionHandler.class, OpenApiConfig.class})
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-that-is-at-least-256-bits-long-for-hs256",
        "jwt.expiration=3600000"
})
class ApiControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtTokenProvider jwtTokenProvider;

    @MockitoBean AuthService authService;
    @MockitoBean AdminUserDetailsService userDetailsService;
    @MockitoBean ProfileService profileService;
    @MockitoBean ExperienceService experienceService;
    @MockitoBean ProjectService projectService;
    @MockitoBean SkillService skillService;
    @MockitoBean EducationService educationService;
    @MockitoBean CertificationService certificationService;
    @MockitoBean LanguageService languageService;
    @MockitoBean StorageService storageService;

    private String token;

    @BeforeEach
    void createAdminToken() {
        var admin = User.withUsername("admin").password("ignored").roles("ADMIN").build();
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(admin);
        token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities()));
    }

    @Test
    void publicPortfolioIsAccessibleWithoutAuthentication() throws Exception {
        when(skillService.getAllSkills()).thenReturn(List.of(new SkillResponse(1L, "Java", "BACKEND", "EXPERT", 1)));

        mockMvc.perform(get("/api/v1/portfolio/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"));
    }

    @Test
    void adminEndpointsRequireJwtAndAcceptValidToken() throws Exception {
        when(skillService.getAllSkills()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/admin/skills"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/admin/skills").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void adminEndpointsRejectAuthenticatedNonAdminUsers() throws Exception {
        var user = User.withUsername("viewer").password("ignored").roles("USER").build();
        when(userDetailsService.loadUserByUsername("viewer")).thenReturn(user);
        String viewerToken = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        mockMvc.perform(get("/api/v1/admin/skills")
                        .header("Authorization", "Bearer " + viewerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void loginIsPublicAndReturnsTokenResponse() throws Exception {
        when(authService.login(any())).thenReturn(new AuthResponse("signed.jwt", "admin", 3600000));

        mockMvc.perform(post("/api/v1/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("signed.jwt"))
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void failedLoginUsesAuthenticationErrorContract() throws Exception {
        when(authService.login(any())).thenThrow(new BadCredentialsException("bad credentials"));

        mockMvc.perform(post("/api/v1/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_FAILED"))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void validationErrorsUseStableApiErrorShape() throws Exception {
        mockMvc.perform(post("/api/v1/admin/skills")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"category\":null,\"proficiency\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/v1/admin/skills"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.violations.length()").value(3))
                .andExpect(jsonPath("$.violations[0].field").value("category"));
    }

    @Test
    void representativeCrudDelegatesAndReturnsExpectedStatuses() throws Exception {
        var response = new SkillResponse(7L, "Spring Boot", "BACKEND", "ADVANCED", 2);
        when(skillService.createSkill(any())).thenReturn(response);
        when(skillService.updateSkill(eq(7L), any())).thenReturn(response);
        doNothing().when(skillService).deleteSkill(7L);
        String body = "{\"name\":\"Spring Boot\",\"category\":\"BACKEND\",\"proficiency\":\"ADVANCED\",\"sortOrder\":2}";

        mockMvc.perform(post("/api/v1/admin/skills").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(7));
        mockMvc.perform(put("/api/v1/admin/skills/7").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Spring Boot"));
        mockMvc.perform(delete("/api/v1/admin/skills/7").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        verify(skillService).deleteSkill(7L);
    }

    @Test
    void representativeCrudFailureUsesNotFoundContract() throws Exception {
        when(skillService.getSkill(99L)).thenThrow(new ResourceNotFoundException("Skill not found with id: 99"));

        mockMvc.perform(get("/api/v1/admin/skills/99").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/api/v1/admin/skills/99"));
    }

    @Test
    void storageFailureReturnsDocumentedGatewayError() throws Exception {
        var file = new MockMultipartFile("file", "avatar.png", MediaType.IMAGE_PNG_VALUE, new byte[]{1});
        when(storageService.uploadFile(any(), eq("portfolio")))
                .thenThrow(new StorageException("Cloud storage upload failed", new RuntimeException("offline")));

        mockMvc.perform(multipart("/api/v1/admin/storage/upload").file(file)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value("STORAGE_ERROR"))
                .andExpect(jsonPath("$.message").value("Cloud storage upload failed"))
                .andExpect(jsonPath("$.violations").isArray());
    }
}
