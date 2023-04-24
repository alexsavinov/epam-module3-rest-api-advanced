package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.model.dto.CreateTagRequest;
import com.epam.esm.epammodule3.model.dto.TagDto;
import com.epam.esm.epammodule3.model.dto.UpdateTagRequest;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.service.TagService;
import com.epam.esm.epammodule3.service.mapper.TagMapper;
import com.epam.esm.epammodule3.util.GenerateInitialData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;
    private final GenerateInitialData generateInitialData;

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable Long id) {
        // TODO remove after test
        if (id.equals(111L)) {
            generateInitialData.generate(10);
        }

        Tag tag = tagService.findById(id);
        TagDto tagDto = tagMapper.toDto(tag);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @GetMapping
    public Page<TagDto> getProducts(Pageable pageable) {
        Page<Tag> tags = tagService.findAll(pageable);

        return tags.map(tagMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto addTag(@RequestBody CreateTagRequest createTagRequest) {
        Tag tag = tagService.create(createTagRequest);
        TagDto tagDto = tagMapper.toDto(tag);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @PatchMapping
    public TagDto updateTag(@RequestBody UpdateTagRequest updateTagRequest) {
        Tag updatedTag = tagService.update(updateTagRequest);
        TagDto tagDto = tagMapper.toDto(updatedTag);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable Long id) {
        tagService.delete(id);
    }

}
