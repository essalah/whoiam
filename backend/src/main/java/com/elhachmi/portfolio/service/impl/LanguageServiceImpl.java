package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.LanguageRequest;
import com.elhachmi.portfolio.dto.response.LanguageResponse;
import com.elhachmi.portfolio.entity.Language;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.LanguageMapper;
import com.elhachmi.portfolio.repository.LanguageRepository;
import com.elhachmi.portfolio.service.LanguageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    @Override
    public List<LanguageResponse> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toResponse)
                .toList();
    }

    @Override
    public LanguageResponse getLanguage(Long id) {
        return languageMapper.toResponse(findLanguageById(id));
    }

    @Override
    @Transactional
    public LanguageResponse createLanguage(LanguageRequest request) {
        Language language = languageMapper.toEntity(request);
        language = languageRepository.save(language);
        return languageMapper.toResponse(language);
    }

    @Override
    @Transactional
    public LanguageResponse updateLanguage(Long id, LanguageRequest request) {
        Language language = findLanguageById(id);
        languageMapper.updateEntityFromRequest(request, language);
        return languageMapper.toResponse(languageRepository.save(language));
    }

    @Override
    @Transactional
    public void deleteLanguage(Long id) {
        languageRepository.delete(findLanguageById(id));
    }

    private Language findLanguageById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
    }
}
