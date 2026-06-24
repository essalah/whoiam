package com.elhachmi.portfolio.identity;

import com.elhachmi.portfolio.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAuthServiceTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("06d53a27-7164-4ddb-8fe4-a75d7f626aae");
    private static final String NORMALIZED_EMAIL = "alice@example.com";
    private static final long TOKEN_EXPIRATION_MS = 3_600_000L;

    @Mock
    private UserAccountRepository userAccountRepository;

    private CustomerAccountDetailsService accountDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private CustomerAuthService customerAuthService;

    @BeforeEach
    void setUp() {
        accountDetailsService = new CustomerAccountDetailsService(userAccountRepository);
        customerAuthService = new CustomerAuthService(
                userAccountRepository,
                accountDetailsService,
                passwordEncoder,
                jwtTokenProvider
        );
    }

    @Test
    void registerNormalizesEmailHashesPasswordRecordsTermsAndIssuesToken() {
        RegisterRequest request = new RegisterRequest(
                "  Alice@Example.COM  ",
                "correct-horse-battery-staple",
                "  Alice Example  ",
                "  v1.0  "
        );
        UserAccount persistedAccount = activeAccount();
        Instant beforeRegistration = Instant.now();

        when(userAccountRepository.existsByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(persistedAccount);
        when(userAccountRepository.findByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(Optional.of(persistedAccount));
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("customer.jwt");
        when(jwtTokenProvider.getExpiration()).thenReturn(TOKEN_EXPIRATION_MS);

        CustomerAuthResponse response = customerAuthService.register(request);

        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        verify(userAccountRepository).saveAndFlush(accountCaptor.capture());
        UserAccount savedAccount = accountCaptor.getValue();
        assertThat(savedAccount.getEmail()).isEqualTo("Alice@Example.COM");
        assertThat(savedAccount.getNormalizedEmail()).isEqualTo(NORMALIZED_EMAIL);
        assertThat(savedAccount.getPasswordHash()).isNotEqualTo(request.password());
        assertThat(savedAccount.getDisplayName()).isEqualTo("Alice Example");
        assertThat(savedAccount.getTermsVersion()).isEqualTo("v1.0");
        assertThat(savedAccount.getTermsAcceptedAt())
                .isBetween(beforeRegistration, Instant.now());
        assertThat(savedAccount.getStatus()).isEqualTo(UserAccountStatus.ACTIVE);
        assertThat(savedAccount.getRole()).isEqualTo(UserAccount.DEFAULT_ROLE);
        verify(passwordEncoder).encode(request.password());

        assertThat(response).isEqualTo(new CustomerAuthResponse(
                "customer.jwt",
                ACCOUNT_ID,
                "Alice@Example.COM",
                "Alice Example",
                TOKEN_EXPIRATION_MS
        ));
    }

    @Test
    void registerRejectsAnExistingNormalizedEmail() {
        RegisterRequest request = new RegisterRequest(
                "Alice@Example.COM",
                "correct-horse-battery-staple",
                null,
                "v1.0"
        );
        when(userAccountRepository.existsByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> customerAuthService.register(request))
                .isInstanceOf(EmailAlreadyRegisteredException.class);

        verify(userAccountRepository, never()).saveAndFlush(any());
        verifyNoInteractions(passwordEncoder, jwtTokenProvider);
    }

    @Test
    void registerRejectsAStaleTermsVersion() {
        RegisterRequest request = new RegisterRequest(
                NORMALIZED_EMAIL, "valid-password", null, "v0.9"
        );

        assertThatThrownBy(() -> customerAuthService.register(request))
                .isInstanceOf(InvalidRegistrationException.class)
                .hasMessageContaining("Terms of Service");

        verifyNoInteractions(userAccountRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void registerRejectsPasswordsOverTheBcryptByteLimit() {
        RegisterRequest request = new RegisterRequest(
                NORMALIZED_EMAIL, "😀".repeat(19), null, "v1.0"
        );

        assertThatThrownBy(() -> customerAuthService.register(request))
                .isInstanceOf(InvalidRegistrationException.class)
                .hasMessageContaining("72 UTF-8 bytes");

        verifyNoInteractions(userAccountRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void loginReportsUnknownAccountAsGenericBadCredentials() {
        CustomerLoginRequest request = new CustomerLoginRequest(NORMALIZED_EMAIL, "password");
        when(userAccountRepository.findByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(Optional.empty());

        assertGenericBadCredentials(request);

        verify(passwordEncoder).matches(request.password(),
                "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void loginReportsInactiveAccountAsGenericBadCredentials() {
        CustomerLoginRequest request = new CustomerLoginRequest(NORMALIZED_EMAIL, "password");
        UserAccount inactiveAccount = activeAccount();
        inactiveAccount.setStatus(UserAccountStatus.DISABLED);
        when(userAccountRepository.findByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(Optional.of(inactiveAccount));

        assertGenericBadCredentials(request);

        verify(passwordEncoder).matches(request.password(),
                "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void loginRejectsWrongPasswordWithGenericBadCredentials() {
        CustomerLoginRequest request = new CustomerLoginRequest(NORMALIZED_EMAIL, "wrong-password");
        UserAccount account = activeAccount();
        when(userAccountRepository.findByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(request.password(), account.getPasswordHash())).thenReturn(false);

        assertGenericBadCredentials(request);

        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void loginIssuesTokenForValidActiveAccount() {
        CustomerLoginRequest request = new CustomerLoginRequest("Alice@Example.COM", "valid-password");
        UserAccount account = activeAccount();
        when(userAccountRepository.findByNormalizedEmail(NORMALIZED_EMAIL)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(request.password(), account.getPasswordHash())).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("customer.jwt");
        when(jwtTokenProvider.getExpiration()).thenReturn(TOKEN_EXPIRATION_MS);

        CustomerAuthResponse response = customerAuthService.login(request);

        assertThat(response).isEqualTo(new CustomerAuthResponse(
                "customer.jwt",
                ACCOUNT_ID,
                "Alice@Example.COM",
                "Alice Example",
                TOKEN_EXPIRATION_MS
        ));
        ArgumentCaptor<Authentication> authenticationCaptor = ArgumentCaptor.forClass(Authentication.class);
        verify(jwtTokenProvider).generateToken(authenticationCaptor.capture());
        assertThat(authenticationCaptor.getValue().getPrincipal())
                .isInstanceOfSatisfying(UserDetails.class,
                        principal -> assertThat(principal.getUsername()).isEqualTo(NORMALIZED_EMAIL));
        assertThat(authenticationCaptor.getValue().getAuthorities())
                .extracting("authority")
                .containsExactly(UserAccount.DEFAULT_ROLE);
    }

    private void assertGenericBadCredentials(CustomerLoginRequest request) {
        assertThatThrownBy(() -> customerAuthService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid email or password");
    }

    private UserAccount activeAccount() {
        return UserAccount.builder()
                .id(ACCOUNT_ID)
                .email("Alice@Example.COM")
                .normalizedEmail(NORMALIZED_EMAIL)
                .displayName("Alice Example")
                .passwordHash("stored-password-hash")
                .status(UserAccountStatus.ACTIVE)
                .role(UserAccount.DEFAULT_ROLE)
                .build();
    }
}
