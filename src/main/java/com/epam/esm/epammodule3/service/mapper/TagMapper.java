package com.epam.esm.epammodule3.service.mapper;

import com.epam.esm.epammodule3.model.dto.CreateTagRequest;
import com.epam.esm.epammodule3.model.dto.TagDto;
import com.epam.esm.epammodule3.model.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public Tag toTag(CreateTagRequest createRequest) {
        return Tag.builder()
                .name(createRequest.getName())
                .build();
    }

    public TagDto toDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
