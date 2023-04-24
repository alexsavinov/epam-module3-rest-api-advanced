package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {
}
