package com.epam.esm.web.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.dao.exception.DaoException;
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
    public CollectionModel<Tag> getTags(@RequestParam int page, @RequestParam int size)
            throws DaoException {
        return tagProcessor.setLinksToTagsPages(
                tagService.getAllTagsByPage(page, size), page, size, tagService.getLastPage(size));
    }

    @GetMapping("/{id}")
    public TagDto getTag(@PathVariable int id) throws DaoException {
        return tagProcessor.setLinksForTag(tagService.getTag(id));
    }

    @PostMapping
    public TagDto addTag(@RequestBody Tag tag) throws DaoException {
        return tagProcessor.setLinksForTag(tagService.addTag(tag));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTag(@PathVariable int id) throws DaoException {
        tagService.deleteTag(id);
        return HttpStatus.OK;
    }
}
