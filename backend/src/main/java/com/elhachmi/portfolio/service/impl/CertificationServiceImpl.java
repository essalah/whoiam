package com.elhachmi.portfolio.service.impl;

import com.elhachmi.portfolio.dto.request.CertificationRequest;
import com.elhachmi.portfolio.dto.response.CertificationResponse;
import com.elhachmi.portfolio.entity.Certification;
import com.elhachmi.portfolio.exception.ResourceNotFoundException;
import com.elhachmi.portfolio.mapper.CertificationMapper;
import com.elhachmi.portfolio.repository.CertificationRepository;
import com.elhachmi.portfolio.service.CertificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final CertificationMapper certificationMapper;

    public CertificationServiceImpl(CertificationRepository certificationRepository,
                                    CertificationMapper certificationMapper) {
        this.certificationRepository = certificationRepository;
        this.certificationMapper = certificationMapper;
    }

    @Override
    public List<CertificationResponse> getAllCertifications() {
        return certificationRepository.findAllByOrderBySortOrderAsc().stream()
                .map(certificationMapper::toResponse)
                .toList();
    }

    @Override
    public CertificationResponse getCertification(Long id) {
        return certificationMapper.toResponse(findCertificationById(id));
    }

    @Override
    @Transactional
    public CertificationResponse createCertification(CertificationRequest request) {
        Certification certification = certificationMapper.toEntity(request);
        certification = certificationRepository.save(certification);
        return certificationMapper.toResponse(certification);
    }

    @Override
    @Transactional
    public CertificationResponse updateCertification(Long id, CertificationRequest request) {
        Certification certification = findCertificationById(id);
        certificationMapper.updateEntityFromRequest(request, certification);
        return certificationMapper.toResponse(certificationRepository.save(certification));
    }

    @Override
    @Transactional
    public void deleteCertification(Long id) {
        certificationRepository.delete(findCertificationById(id));
    }

    private Certification findCertificationById(Long id) {
        return certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + id));
    }
}
