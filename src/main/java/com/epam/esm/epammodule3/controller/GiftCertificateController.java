package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.model.dto.*;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.service.GiftCertificateService;
import com.epam.esm.epammodule3.service.mapper.GiftCertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/certificates"})
public class GiftCertificateController {

    private final GiftCertificateMapper certificateMapper;
    private final GiftCertificateService certificateService;

    @GetMapping(value = "/{id}")
    public GiftCertificateDto getCertificateById(@PathVariable Long id) {
        GiftCertificate certificate = certificateService.findById(id);
        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);

        certificateDto.add(linkTo(methodOn(GiftCertificateController.class).getCertificateById(certificateDto.getId()))
                .withSelfRel());
        return certificateDto;
    }

    @GetMapping
    public Page<GiftCertificateDto> getAllCertificates(Pageable pageable) {
        Page<GiftCertificate> certificates = certificateService.findAll(pageable);

        return certificates.map(certificateMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto addCertificate(@RequestBody CreateGiftCertificateRequest createCertificateRequest) {
        GiftCertificate certificate = certificateService.create(createCertificateRequest);
        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);

        certificateDto.add(linkTo(methodOn(GiftCertificateController.class).getCertificateById(certificateDto.getId()))
                .withSelfRel());
        return certificateDto;
    }

    @PatchMapping
    public GiftCertificateDto updateCertificate(@RequestBody UpdateGiftCertificateRequest updateRequest) {
        GiftCertificate updatedCertificate = certificateService.update(updateRequest);
        GiftCertificateDto certificateDto = certificateMapper.toDto(updatedCertificate);

        certificateDto.add(linkTo(methodOn(GiftCertificateController.class).getCertificateById(certificateDto.getId()))
                .withSelfRel());
        return certificateDto;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificateById(@PathVariable Long id) {
        certificateService.delete(id);
    }

    @GetMapping(value = "/search")
    public Page<GiftCertificateDto> searchCertificatesWithSearchParams(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> tag,
            Pageable pageable) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setTags(tag);
        searchRequest.setName(name);
        searchRequest.setDescription(description);

        Page<GiftCertificate> certificates = certificateService
                .findCertificateWithSearchParams(pageable, searchRequest);

        return certificates.map(certificateMapper::toDto);
    }
}