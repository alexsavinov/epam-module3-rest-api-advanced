package com.epam.esm.epammodule3.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRequest {

    private String name;
    private String description;
    private List<String> tags;
}
