package com.epam.esm.web.hateoas;

import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinkBuilder implements ModelLinkBuilder<TagDto>{

    private static final String ALL_TAGS = "tags";
    private static final String CURRENT_TAG = "tag";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final TagRequestBody defaultRequestBody = TagRequestBody.getDefaultTagRequestBody();

    @Override
    public void linkToModel(EntityModel<TagDto> modelDto) throws ServiceException {
        modelDto.add(linkTo(methodOn(TagController.class).getTag(
                Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_TAG));
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<TagDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToTagsPage(page, size, ALL_TAGS));
    }

    @Override
    public void linkToFirstModelPage(EntityModel<TagDto> tagDto) throws ServiceException {
        tagDto.add(getLinkToTagsPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_TAGS));
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<TagDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToTagsPage(page + 1, size, "next"));
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<TagDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToTagsPage(page - 1, size, "prev"));
    }

    private Link getLinkToTagsPage(int page, int size, String rel) throws ServiceException {
        return linkTo(methodOn(TagController.class).getTags(defaultRequestBody, page, size)).withRel(rel);
    }
}
