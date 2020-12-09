package com.epam.esm.web.hateoas;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.model.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagProcessorImpl implements TagProcessor {

    private static final String ALL_TAGS = "tags";
    private static final String CURRENT_TAG = "tag";

    private final TagRequestBody defaultRequestBody = TagRequestBody.getDefault();

    public CollectionModel<Tag> processGetTags(List<Tag> tags, TagRequestBody request) throws PersistenceException {
        CollectionModel<Tag> tagCollectionModel = CollectionModel.of(tags);
        linkToTags(tagCollectionModel, request);

        return tagCollectionModel;
    }

    public TagDto processGetTag(Tag tag) throws PersistenceException {
        TagDto tagDto = TagDto.of(tag);
        linkToTag(tagDto);
        linkToTags(tagDto, defaultRequestBody);

        return tagDto;
    }

    public TagDto processAddTag(Tag tag) throws PersistenceException {
        TagDto tagDto = TagDto.of(tag);
        linkToTag(tagDto);
        linkToTags(tagDto, defaultRequestBody);

        return tagDto;
    }

    private void linkToSelf(TagDto tagDto) {
        tagDto.add(linkTo(TagController.class).slash(tagDto.getId()).withSelfRel());
    }

    private void linkToTags(CollectionModel<Tag> tagCollectionModel, TagRequestBody request)
            throws PersistenceException {
        tagCollectionModel.add(linkTo(methodOn(TagController.class).getTags(request)).withRel(ALL_TAGS));
    }

    private void linkToTags(TagDto tagDto, TagRequestBody request) throws PersistenceException {
        tagDto.add(linkTo(methodOn(TagController.class).getTags(request)).withRel(ALL_TAGS));
    }

    private void linkToTag(TagDto tagDto) throws PersistenceException {
        tagDto.add(linkTo(methodOn(TagController.class).getTag(tagDto.getId())).withRel(CURRENT_TAG));
    }
}
