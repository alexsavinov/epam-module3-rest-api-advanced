package com.epam.esm.epammodule3.service;


import com.epam.esm.epammodule3.exception.TagAlreadyExistsException;
import com.epam.esm.epammodule3.exception.TagNotFoundException;
import com.epam.esm.epammodule3.model.dto.CreateTagRequest;
import com.epam.esm.epammodule3.model.dto.UpdateTagRequest;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.repository.PageableTagRepository;
import com.epam.esm.epammodule3.repository.TagRepository;
import com.epam.esm.epammodule3.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PageableTagRepository pageableTagRepository;
    private final TagMapper tagMapper;

    @Override
    public Tag findById(Long id) {
        log.debug("Looking for a tag with id {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Requested resource not found (id = %s)"
                        .formatted(id)
                ));

        log.info("Retrieved a tag with id {}", id);
        return tag;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        log.debug("Retrieving tags. Page request: {}", pageable);

        Page<Tag> tags = pageableTagRepository.findAll(pageable);

        log.info("Retrieved {} tags of {} total", tags.getSize(), tags.getTotalElements());
        return tags;
    }

    @Override
    public Tag create(CreateTagRequest createRequest) {
        log.debug("Creating a new tag");

        Tag tag = tagMapper.toTag(createRequest);
        Tag createdTag;

        try {
            createdTag = tagRepository.save(tag);

            log.info("Created a new tag with id {}", createdTag.getId());
            return createdTag;
        } catch (DataIntegrityViolationException ex) {
            throw new TagAlreadyExistsException(
                    "Requested resource already exists (name = %s)".formatted(createRequest.getName()));
        }
    }

    @Override
    @Transactional
    public Tag update(UpdateTagRequest updateRequest) {
        log.debug("Updating a tag with id {}", updateRequest.getId());
        Tag tag = findById(updateRequest.getId());

        tag.setName(updateRequest.getName());

        Tag updatedTag = tagRepository.save(tag);

        log.info("Updated a tag with id {}", updatedTag.getId());
        return updatedTag;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting tag with id {}", id);
        Tag tag = findById(id);

        tagRepository.delete(tag);
        log.info("Tag with id {} is deleted", tag.getId());
    }

    @Override
    public Tag findByName(String name) {
        log.debug("Looking for a tag with name {}", name);
        Tag tag = tagRepository.findFirstByName(name);

        if (tag == null) {
            throw new TagNotFoundException("Requested resource not found (name = %s)".formatted(name));
        }

        log.info("Found a tag with name {}", name);
        return tag;
    }

    @Override
    public Tag createTagWithCheck(CreateTagRequest createTagRequest) {
        String tagName = createTagRequest.getName();
        log.debug("Looking for a tag with name {}", tagName);

        Tag tag = tagRepository.findFirstByName(tagName);

        if (tag == null) {
            Tag tagNew = tagMapper.toTag(createTagRequest);
            tag = tagRepository.save(tagNew);
            log.info("Created a new tag with name {}", tagName);
        } else {
            log.debug("Found a tag with name {}", tagName);
        }

        return tag;
    }
}
