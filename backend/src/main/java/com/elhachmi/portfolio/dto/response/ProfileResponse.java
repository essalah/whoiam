package com.elhachmi.portfolio.dto.response;

import java.util.List;

public record ProfileResponse(
        Long id,
        String name,
        String title,
        String summary,
        String email,
        String phone,
        String location,
        String website,
        String avatarUrl,
        List<SocialLinkResponse> socialLinks
) {}
