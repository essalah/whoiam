package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.LanguageRequest;
import com.elhachmi.portfolio.dto.response.LanguageResponse;
import com.elhachmi.portfolio.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageResponse toResponse(Language language) {
        if (language == null) {
            return null;
        }

        return new LanguageResponse(
                language.getId(),
                language.getName(),
                language.getProficiency().name()
        );
    }

    public Language toEntity(LanguageRequest request) {
        if (request == null) {
            return null;
        }

        Language language = new Language();
        updateEntityFromRequest(request, language);
        return language;
    }

    public void updateEntityFromRequest(LanguageRequest request, Language language) {
        if (request == null || language == null) {
            return;
        }

        language.setName(request.name());
        language.setProficiency(request.proficiency());
    }
}
