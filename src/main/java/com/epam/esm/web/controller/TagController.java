package com.epam.esm.web.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.dao.exception.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_OFFSET = 0;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public CollectionModel<Tag> getTags(@RequestParam int limit, @RequestParam int offset)
            throws PersistenceException {
        CollectionModel<Tag> tags = CollectionModel.of(tagService.getAllTagsByPage(limit, offset));
        tags.add(linkTo(methodOn(TagController.class).getTags(limit, offset)).withSelfRel());
        return tags;
    }

    @GetMapping("/{id}")
    public Tag getTag(@PathVariable int id) throws PersistenceException {
        Tag tag = tagService.getTag(id);
        tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel());
        tag.add(linkTo(methodOn(TagController.class).getTags(DEFAULT_LIMIT, DEFAULT_OFFSET)).withRel("tags"));
        return tag;
    }

    @PostMapping
    public Tag addTag(@RequestBody Tag tag) throws PersistenceException {
        tag = tagService.addTag(tag);
        tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withRel("addedTag"));
        tag.add(linkTo(methodOn(TagController.class).getTags(DEFAULT_LIMIT, DEFAULT_OFFSET)).withRel("tags"));
        return tag;
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTag(@PathVariable int id) throws PersistenceException {
        tagService.deleteTag(id);
        return HttpStatus.OK;
    }
}
