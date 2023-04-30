package com.epam.esm.epammodule3.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {

    private Long userId;
    private Long certificateId;
}
