package com.epam.esm.epammodule3.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateGiftCertificateRequest {

    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private List<CreateTagRequest> tags;
}
