package com.epam.esm.epammodule3.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchGiftCertificateRequest {

    private String name;
    private String description;
    private List<String> tags;
}
