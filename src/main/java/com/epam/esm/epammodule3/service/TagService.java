package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.dto.CreateTagRequest;
import com.epam.esm.epammodule3.model.dto.UpdateTagRequest;
import com.epam.esm.epammodule3.model.entity.Tag;

import java.util.Optional;

public interface TagService extends PageableTagService{

    Tag findById(Long id);

    Tag create(CreateTagRequest createRequest);

    Tag createTagWithCheck(CreateTagRequest createTagRequest);

    Tag update(UpdateTagRequest updateRequest);

    void delete(Long id);

    Tag findByName(String name);

    Optional<Tag> getTopUsedTag(Long id);
}
