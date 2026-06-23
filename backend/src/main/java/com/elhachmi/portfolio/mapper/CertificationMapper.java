package com.elhachmi.portfolio.mapper;

import com.elhachmi.portfolio.dto.request.CertificationRequest;
import com.elhachmi.portfolio.dto.response.CertificationResponse;
import com.elhachmi.portfolio.entity.Certification;
import org.springframework.stereotype.Component;

@Component
public class CertificationMapper {

    public CertificationResponse toResponse(Certification certification) {
        if (certification == null) {
            return null;
        }

        return new CertificationResponse(
                certification.getId(),
                certification.getName(),
                certification.getIssuer(),
                certification.getIssueDate(),
                certification.getUrl(),
                certification.getSortOrder()
        );
    }

    public Certification toEntity(CertificationRequest request) {
        if (request == null) {
            return null;
        }

        Certification certification = new Certification();
        updateEntityFromRequest(request, certification);
        return certification;
    }

    public void updateEntityFromRequest(CertificationRequest request, Certification certification) {
        if (request == null || certification == null) {
            return;
        }

        certification.setName(request.name());
        certification.setIssuer(request.issuer());
        certification.setIssueDate(request.issueDate());
        certification.setUrl(request.url());
        
        if (request.sortOrder() != null) {
            certification.setSortOrder(request.sortOrder());
        }
    }
}
