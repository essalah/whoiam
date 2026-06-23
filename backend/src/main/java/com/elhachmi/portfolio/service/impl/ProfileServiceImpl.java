package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.ProfileRequest;
import com.elhachmi.portfolio.dto.response.ProfileResponse;
import com.elhachmi.portfolio.entity.Profile;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.ProfileMapper;
import com.elhachmi.portfolio.repository.ProfileRepository;
import com.elhachmi.portfolio.service.ProfileService;
import com.elhachmi.portfolio.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final StorageService storageService;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper, StorageService storageService) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.storageService = storageService;
    }

    @Override
    public ProfileResponse getProfile() {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(Long id, ProfileRequest request) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        profileMapper.updateEntityFromRequest(request, profile);
        profile = profileRepository.save(profile);
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public String uploadAvatar(Long id, MultipartFile file) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        String avatarUrl = storageService.uploadFile(file, "profile-avatars");
        profile.setAvatarUrl(avatarUrl);
        profileRepository.save(profile);
        return avatarUrl;
    }
}
