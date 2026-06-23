package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.LoginRequest;
import com.elhachmi.portfolio.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}
