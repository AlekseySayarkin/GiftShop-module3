package com.epam.esm.web.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.dao.exception.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/tags")
    public List<Tag> getTags(@RequestParam int limit, @RequestParam int offset)
            throws PersistenceException {
        return tagService.getAllTagsByPage(limit, offset);
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable int id) throws PersistenceException {
        return tagService.getTag(id);
    }

    @PostMapping("/tags")
    public Tag addTag(@RequestBody Tag tag) throws PersistenceException {
        return tagService.addTag(tag);
    }

    @DeleteMapping("/tags/{id}")
    public HttpStatus deleteTag(@PathVariable int id) throws PersistenceException {
        tagService.deleteTag(id);
        return HttpStatus.OK;
    }
}
