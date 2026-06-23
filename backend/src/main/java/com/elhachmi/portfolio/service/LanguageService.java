package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.LanguageRequest;
import com.elhachmi.portfolio.dto.response.LanguageResponse;

import java.util.List;

public interface LanguageService {

    List<LanguageResponse> getAllLanguages();

    LanguageResponse getLanguage(Long id);

    LanguageResponse createLanguage(LanguageRequest request);

    LanguageResponse updateLanguage(Long id, LanguageRequest request);

    void deleteLanguage(Long id);
}
