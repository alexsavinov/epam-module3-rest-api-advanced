package com.epam.esm.epammodule3.model.dto.request;

import com.epam.esm.epammodule3.model.dto.GiftCertificateDto;
import com.epam.esm.epammodule3.model.dto.UserDto;
import lombok.Getter;

@Getter
public class UpdateOrderRequest {

    private Long id;
    private Double price;
    private UserDto user;
    private GiftCertificateDto certificate;
}
