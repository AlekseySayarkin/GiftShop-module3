package com.epam.esm.web.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.web.hateoas.TagProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final TagProcessor tagProcessor;

    @Autowired
    public TagController(TagService tagService, TagProcessor tagProcessor) {
        this.tagService = tagService;
        this.tagProcessor = tagProcessor;
    }

    @GetMapping
    public CollectionModel<Tag> getTags(@RequestBody TagRequestBody request)
            throws PersistenceException {
        return tagProcessor.processGetTags(tagService.getAllTagsByPage(request), request);
    }

    @GetMapping("/{id}")
    public TagDto getTag(@PathVariable int id) throws PersistenceException {
        return tagProcessor.processGetTag(tagService.getTag(id));
    }

    @PostMapping
    public TagDto addTag(@RequestBody Tag tag) throws PersistenceException {
        return tagProcessor.processAddTag(tagService.addTag(tag));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTag(@PathVariable int id) throws PersistenceException {
        tagService.deleteTag(id);
        return HttpStatus.OK;
    }
}
