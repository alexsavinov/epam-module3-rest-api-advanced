package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PageableGiftCertificateRepository
        extends JpaRepository<GiftCertificate, Long>, JpaSpecificationExecutor<GiftCertificate> {
}
