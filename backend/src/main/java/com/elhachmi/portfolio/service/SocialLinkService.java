package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.SocialLinkRequest;
import com.elhachmi.portfolio.dto.response.SocialLinkResponse;

import java.util.List;

public interface SocialLinkService {

    List<SocialLinkResponse> getAllLinks(Long profileId);

    SocialLinkResponse getLink(Long id);

    SocialLinkResponse createLink(Long profileId, SocialLinkRequest request);

    SocialLinkResponse updateLink(Long id, SocialLinkRequest request);

    void deleteLink(Long id);
}
