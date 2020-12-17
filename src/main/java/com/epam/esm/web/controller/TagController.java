package com.epam.esm.web.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final ModelAssembler<TagDto> modelAssembler;

    @Autowired
    public TagController(TagService tagService, ModelAssembler<TagDto> modelAssembler) {
        this.tagService = tagService;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<TagDto>> getTags(
            @RequestBody(required = false) TagRequestBody requestBody,
            @RequestParam int page, @RequestParam int size) throws ServiceException {

        int lastPage = tagService.getLastPage(size);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                size, page, (long) lastPage * size, lastPage);
        modelAssembler.setMetadata(pageMetadata);
        return modelAssembler.toCollectionModel(TagDto.of(tagService.getAllTagsByPage(requestBody, page, size)));
    }

    @GetMapping("/{id}")
    public EntityModel<TagDto> getTag(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(TagDto.of(tagService.getTag(id)));
    }

    @PostMapping
    public EntityModel<TagDto> addTag(@RequestBody Tag tag) throws ServiceException {
        return modelAssembler.toModel(TagDto.of(tagService.addTag(tag)));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTag(@PathVariable int id) throws ServiceException {
        tagService.deleteTag(id);
        return HttpStatus.OK;
    }
}
