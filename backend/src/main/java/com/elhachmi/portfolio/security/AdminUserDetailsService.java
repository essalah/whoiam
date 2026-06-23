package com.elhachmi.portfolio.security;

import com.elhachmi.portfolio.entity.AdminUser;
import com.elhachmi.portfolio.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found: " + username));

        return User.builder()
                .username(adminUser.getUsername())
                .password(adminUser.getPasswordHash())
                .roles(adminUser.getRole().replace("ROLE_", ""))
                .disabled(!adminUser.getEnabled())
                .build();
    }
}
