package com.epam.esm.epammodule3.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Builder
@Getter
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private List<TagDto> tags;
    private String createDate;
    private String lastUpdateDate;

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
}
