package com.epam.esm.epammodule3.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
public class TagDto extends RepresentationModel<TagDto> {

    private Long id;
    private String name;
}
