package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.dto.SearchRequest;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageableGiftCertificateService {

    Page<GiftCertificate> findAll(Pageable pageable);

    Page<GiftCertificate> findCertificateWithSearchParams(Pageable pageable, SearchRequest searchRequest);
}
