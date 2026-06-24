package com.elhachmi.portfolio.identity;

import com.elhachmi.portfolio.security.JwtTokenProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.nio.charset.StandardCharsets;

@Service
public class CustomerAuthService {

    private static final String DUMMY_BCRYPT_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private final UserAccountRepository userAccountRepository;
    private final CustomerAccountDetailsService accountDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private String currentTermsVersion = "v1.0";

    public CustomerAuthService(UserAccountRepository userAccountRepository,
                               CustomerAccountDetailsService accountDetailsService,
                               PasswordEncoder passwordEncoder,
                               JwtTokenProvider jwtTokenProvider) {
        this.userAccountRepository = userAccountRepository;
        this.accountDetailsService = accountDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public CustomerAuthResponse register(RegisterRequest request) {
        validateRegistration(request);
        String normalizedEmail = UserAccount.normalizeEmail(request.email());
        if (userAccountRepository.existsByNormalizedEmail(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException();
        }

        UserAccount account = UserAccount.builder()
                .email(request.email().trim())
                .normalizedEmail(normalizedEmail)
                .displayName(normalizeDisplayName(request.displayName()))
                .passwordHash(passwordEncoder.encode(request.password()))
                .termsVersion(request.termsVersion().trim())
                .termsAcceptedAt(Instant.now())
                .build();

        try {
            account = userAccountRepository.saveAndFlush(account);
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyRegisteredException();
        }

        return issueToken(account);
    }

    @Transactional(readOnly = true)
    public CustomerAuthResponse login(CustomerLoginRequest request) {
        UserAccount account;
        try {
            account = accountDetailsService.loadActiveAccount(request.email());
        } catch (UsernameNotFoundException exception) {
            passwordEncoder.matches(request.password(), DUMMY_BCRYPT_HASH);
            throw invalidCredentials();
        }

        if (!passwordEncoder.matches(request.password(), account.getPasswordHash())) {
            throw invalidCredentials();
        }

        return issueToken(account);
    }

    private CustomerAuthResponse issueToken(UserAccount account) {
        UserDetails principal = accountDetailsService.loadActiveUserDetails(account.getNormalizedEmail());
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                principal,
                null,
                principal.getAuthorities()
        );
        String token = jwtTokenProvider.generateToken(authentication);
        return new CustomerAuthResponse(
                token,
                account.getId(),
                account.getEmail(),
                account.getDisplayName(),
                jwtTokenProvider.getExpiration()
        );
    }

    private String normalizeDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            return null;
        }
        return displayName.trim();
    }

    @Value("${legal.current-terms-version:v1.0}")
    void setCurrentTermsVersion(String currentTermsVersion) {
        this.currentTermsVersion = currentTermsVersion;
    }

    private void validateRegistration(RegisterRequest request) {
        if (!currentTermsVersion.equals(request.termsVersion().trim())) {
            throw new InvalidRegistrationException("The current Terms of Service must be accepted");
        }
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new InvalidRegistrationException("Password must not exceed 72 UTF-8 bytes");
        }
    }

    private BadCredentialsException invalidCredentials() {
        return new BadCredentialsException("Invalid email or password");
    }
}
