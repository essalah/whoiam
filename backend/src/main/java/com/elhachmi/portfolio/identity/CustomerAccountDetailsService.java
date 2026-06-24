package com.elhachmi.portfolio.identity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomerAccountDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount loadActiveAccount(String email) {
        String normalizedEmail = UserAccount.normalizeEmail(email);
        return userAccountRepository.findByNormalizedEmail(normalizedEmail)
                .filter(UserAccount::isActive)
                .orElseThrow(() -> new UsernameNotFoundException("Customer account not found"));
    }

    public UserDetails loadActiveUserDetails(String email) {
        UserAccount account = loadActiveAccount(email);
        return User.withUsername(account.getNormalizedEmail())
                .password(account.getPasswordHash())
                .authorities(account.getRole())
                .build();
    }
}
