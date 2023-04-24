package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.exception.GiftCertificateNotFoundException;
import com.epam.esm.epammodule3.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.epammodule3.model.dto.CreateTagRequest;
import com.epam.esm.epammodule3.model.dto.SearchRequest;
import com.epam.esm.epammodule3.model.dto.UpdateGiftCertificateRequest;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.repository.*;
import com.epam.esm.epammodule3.service.mapper.GiftCertificateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final PageableGiftCertificateRepository pageableCertificateRepository;
    private final TagService tagService;
    private final GiftCertificateMapper certificateMapper;

    @Override
    public GiftCertificate findById(Long id) {
        log.debug("Looking for a gift certificate with id {}", id);

        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(
                        "Requested resource not found (id = %s)".formatted(id)
                ));

        List<Tag> tags = tagRepository.findTagsByCertificatesId(id);
        certificate.setTags(tags);

        log.info("Received a gift certificate with id {}", id);
        return certificate;
    }

    @Override
    public Page<GiftCertificate> findAll(Pageable pageable) {
        log.debug("Retrieving gift certificates. Page request: {}", pageable);

        Page<GiftCertificate> certificates = pageableCertificateRepository.findAll(pageable);

        log.info("Retrieved {} gift certificates of {} total", certificates.getSize(), certificates.getTotalElements());
        return certificates;
    }

    @Override
    public GiftCertificate create(CreateGiftCertificateRequest createRequest) {
        log.debug("Creating a new certificate");

        GiftCertificate certificate = certificateMapper.toGiftCertificate(createRequest);
        List<Tag> tags = certificate.getTags();

        if (tags != null) {
            CreateTagRequest createTagRequest = new CreateTagRequest();
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                createTagRequest.setName(tag.getName());
                Tag createdTag = tagService.createTagWithCheck(createTagRequest);
                tag.setId(createdTag.getId());
            }
        }

        GiftCertificate createdCertificate = certificateRepository.save(certificate);

        log.info("Created a new gift certificate with id {}", createdCertificate.getId());
        return createdCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(UpdateGiftCertificateRequest updateRequest) {
        log.debug("Updating a gift certificate with id {}", updateRequest.getId());
        GiftCertificate certificate = findById(updateRequest.getId());

        if (updateRequest.getName() != null) {
            certificate.setName(updateRequest.getName());
        }
        if (updateRequest.getDescription() != null) {
            certificate.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getPrice() != null) {
            certificate.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getDuration() != null) {
            certificate.setDuration(updateRequest.getDuration());
        }

        GiftCertificate updatedCertificate = certificateRepository.save(certificate);

        log.info("Updated a gift certificate with id {}", updatedCertificate.getId());
        return updatedCertificate;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting gift certificate with id {}", id);
        GiftCertificate certificate = findById(id);

        certificateRepository.delete(certificate);
        log.info("Gift certificate with id {} is deleted", certificate.getId());
    }

    @Override
    public Page<GiftCertificate> findCertificateWithSearchParams(Pageable pageable, SearchRequest searchRequest) {
        log.debug("Looking for a certificates by search params");

        Specification<GiftCertificate> specification = new GiftCertificateSpecification(searchRequest);

        Page<GiftCertificate> certificates = pageableCertificateRepository.findAll(specification, pageable);

        log.info("Retrieved {} gift certificates of {} total", certificates.getSize(), certificates.getTotalElements());

        return certificates;
    }
}
