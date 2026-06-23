package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.ProfileRequest;
import com.elhachmi.portfolio.dto.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getProfile();

    ProfileResponse updateProfile(Long id, ProfileRequest request);

    String uploadAvatar(Long id, org.springframework.web.multipart.MultipartFile file);
}
