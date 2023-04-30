package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.EpamModule3Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EpamModule3Application.class)
class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Test
    void context() {
        assertThat(certificateRepository).isNotNull();
    }
}