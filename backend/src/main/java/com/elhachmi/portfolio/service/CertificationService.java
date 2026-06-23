package com.elhachmi.portfolio.service;

import com.elhachmi.portfolio.dto.request.CertificationRequest;
import com.elhachmi.portfolio.dto.response.CertificationResponse;

import java.util.List;

public interface CertificationService {

    List<CertificationResponse> getAllCertifications();

    CertificationResponse getCertification(Long id);

    CertificationResponse createCertification(CertificationRequest request);

    CertificationResponse updateCertification(Long id, CertificationRequest request);

    void deleteCertification(Long id);
}
