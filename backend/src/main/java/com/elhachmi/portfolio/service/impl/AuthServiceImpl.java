package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.LoginRequest;
import com.elhachmi.portfolio.dto.response.AuthResponse;
import com.elhachmi.portfolio.security.JwtTokenProvider;
import com.elhachmi.portfolio.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String token = jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(token, authentication.getName(), jwtTokenProvider.getExpiration());
    }
}
