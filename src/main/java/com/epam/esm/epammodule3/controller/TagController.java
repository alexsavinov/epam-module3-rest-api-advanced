package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.model.dto.CreateTagRequest;
import com.epam.esm.epammodule3.model.dto.TagDto;
import com.epam.esm.epammodule3.model.dto.UpdateTagRequest;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable Long id) {
        Tag foundTag = tagService.findById(id);
        TagDto tagDto = modelMapper.map(foundTag, TagDto.class);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @GetMapping
    public Page<TagDto> getTags(Pageable pageable) {
        Page<Tag> foundTags = tagService.findAll(pageable);

        return foundTags.map(tag -> modelMapper.map(tag, TagDto.class));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto addTag(@RequestBody CreateTagRequest createTagRequest) {
        Tag createdTag = tagService.create(createTagRequest);
        TagDto tagDto = modelMapper.map(createdTag, TagDto.class);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @PatchMapping
    public TagDto updateTag(@RequestBody UpdateTagRequest updateTagRequest) {
        Tag updatedTag = tagService.update(updateTagRequest);
        TagDto tagDto = modelMapper.map(updatedTag, TagDto.class);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable Long id) {
        tagService.delete(id);
    }
}
