package com.epam.esm.web.api;

import com.epam.esm.service.criteria.sort.SortBy;
import com.epam.esm.service.criteria.sort.SortType;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.criteria.search.TagSearchCriteria;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.TagLinkBuilder;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import com.epam.esm.web.hateoas.pagination.impl.PaginationConfigurerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final ModelAssembler<TagDto> modelAssembler;
    private final PaginationConfigurer paginationConfigurer;

    @Autowired
    public TagController(TagService tagService, ModelAssembler<TagDto> modelAssembler,
                         PaginationValidator paginationValidator) {
        this.tagService = tagService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new TagLinkBuilder());
    }

    @GetMapping
    public CollectionModel<EntityModel<TagDto>> getTags(
            @RequestBody(required = false) TagSearchCriteria requestBody,
            @RequestParam int page, @RequestParam int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, tagService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                TagDto.of(tagService.getAllTagsByPage(requestBody, page, size, sortType, sortBy))
        );
    }

    @GetMapping("/{id}")
    public EntityModel<TagDto> getTag(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(TagDto.of(tagService.getTagById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tags:write') and #oauth2.hasScope('write')")
    public EntityModel<TagDto> addTag(@RequestBody Tag tag) throws ServiceException {
        return modelAssembler.toModel(TagDto.of(tagService.addTag(tag)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('tags:write') and #oauth2.hasScope('write')")
    public HttpStatus deleteTag(@PathVariable int id) throws ServiceException {
        tagService.deleteTag(id);
        return HttpStatus.OK;
    }
}
