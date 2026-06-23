package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.SocialLinkRequest;
import com.elhachmi.portfolio.dto.response.SocialLinkResponse;
import com.elhachmi.portfolio.entity.Profile;
import com.elhachmi.portfolio.entity.SocialLink;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.ProfileMapper;
import com.elhachmi.portfolio.repository.ProfileRepository;
import com.elhachmi.portfolio.repository.SocialLinkRepository;
import com.elhachmi.portfolio.service.SocialLinkService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SocialLinkServiceImpl implements SocialLinkService {

    private final SocialLinkRepository socialLinkRepository;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public SocialLinkServiceImpl(SocialLinkRepository socialLinkRepository,
                                 ProfileRepository profileRepository,
                                 ProfileMapper profileMapper) {
        this.socialLinkRepository = socialLinkRepository;
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    public List<SocialLinkResponse> getAllLinks(Long profileId) {
        verifyProfileExists(profileId);
        return socialLinkRepository.findByProfileId(profileId).stream()
                .map(profileMapper::toSocialLinkResponse)
                .toList();
    }

    @Override
    public SocialLinkResponse getLink(Long id) {
        return profileMapper.toSocialLinkResponse(findLinkById(id));
    }

    @Override
    public SocialLinkResponse createLink(Long profileId, SocialLinkRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + profileId));

        SocialLink socialLink = new SocialLink();
        socialLink.setPlatform(request.platform());
        socialLink.setUrl(request.url());
        socialLink.setProfile(profile);

        socialLink = socialLinkRepository.save(socialLink);
        return profileMapper.toSocialLinkResponse(socialLink);
    }

    @Override
    public SocialLinkResponse updateLink(Long id, SocialLinkRequest request) {
        SocialLink socialLink = findLinkById(id);
        socialLink.setPlatform(request.platform());
        socialLink.setUrl(request.url());
        return profileMapper.toSocialLinkResponse(socialLinkRepository.save(socialLink));
    }

    @Override
    public void deleteLink(Long id) {
        socialLinkRepository.delete(findLinkById(id));
    }

    private SocialLink findLinkById(Long id) {
        return socialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Social link not found with id: " + id));
    }

    private void verifyProfileExists(Long profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Profile not found with id: " + profileId);
        }
    }
}
