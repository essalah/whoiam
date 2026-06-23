package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.ProfileRequest;
import com.elhachmi.portfolio.dto.response.ProfileResponse;
import com.elhachmi.portfolio.dto.response.SocialLinkResponse;
import com.elhachmi.portfolio.entity.Profile;
import com.elhachmi.portfolio.entity.SocialLink;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileMapper {

    public ProfileResponse toResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        List<SocialLinkResponse> socialLinks = profile.getSocialLinks().stream()
                .map(this::toSocialLinkResponse)
                .toList();

        return new ProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getTitle(),
                profile.getSummary(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getWebsite(),
                profile.getAvatarUrl(),
                socialLinks
        );
    }

    public SocialLinkResponse toSocialLinkResponse(SocialLink link) {
        if (link == null) {
            return null;
        }
        return new SocialLinkResponse(
                link.getId(),
                link.getPlatform().name(),
                link.getUrl()
        );
    }

    public void updateEntityFromRequest(ProfileRequest request, Profile profile) {
        if (request == null || profile == null) {
            return;
        }
        
        profile.setName(request.name());
        profile.setTitle(request.title());
        profile.setSummary(request.summary());
        profile.setEmail(request.email());
        profile.setPhone(request.phone());
        profile.setLocation(request.location());
        profile.setWebsite(request.website());
        profile.setAvatarUrl(request.avatarUrl());
    }
}
