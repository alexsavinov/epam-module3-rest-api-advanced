package com.epam.esm.epammodule3.model.dto.request;

import lombok.Getter;

@Getter
public class UpdateGiftCertificateRequest {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
}
