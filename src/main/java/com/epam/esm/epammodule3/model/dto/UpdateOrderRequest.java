package com.epam.esm.epammodule3.model.dto;

import lombok.Getter;

@Getter
public class UpdateOrderRequest {

    private Long id;
    private Double price;
    private UserDto user;
    private GiftCertificateDto certificate;
}
