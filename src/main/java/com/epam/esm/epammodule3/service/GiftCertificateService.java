package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.epammodule3.model.dto.UpdateGiftCertificateRequest;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;

public interface GiftCertificateService extends PageableGiftCertificateService {

    GiftCertificate findById(Long id);

    GiftCertificate create(CreateGiftCertificateRequest createRequest);

    GiftCertificate update(UpdateGiftCertificateRequest updateRequest);

    void delete(Long id);
}
