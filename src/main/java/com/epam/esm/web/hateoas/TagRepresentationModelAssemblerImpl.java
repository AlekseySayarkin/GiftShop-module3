package com.epam.esm.web.hateoas;

import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagRepresentationModelAssemblerImpl implements ModelAssembler<TagDto> {

    private static final String ALL_TAGS = "tags";
    private static final String CURRENT_TAG = "tag";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final TagRequestBody defaultRequestBody = TagRequestBody.getDefaultTagRequestBody();

    private PagedModel.PageMetadata metadata;

    public void setMetadata(PagedModel.PageMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void addLinks(EntityModel<TagDto> resource) {
        try {
            linkToTag(resource);
            linkToFirstTagsPage(resource);
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<TagDto>> resources) {
        if (metadata != null) {
            int size = (int) metadata.getSize();
            int page = (int) metadata.getNumber();
            int lastPage = (int) metadata.getTotalPages();

            try {
                linkToTagsPage(resources, page, size);
                if (hasNext(page, lastPage)) {
                    linkToNextPage(resources, page, size);
                }
                if (hasPrevious(page)) {
                    linkToPrevPage(resources, page, size);
                }
            } catch (ServiceException ignored) {
            }
        }
    }

    private void linkToTag(EntityModel<TagDto> tagDto) throws ServiceException {
        tagDto.add(linkTo(methodOn(TagController.class).getTag(
                Objects.requireNonNull(tagDto.getContent()).getId())).withRel(CURRENT_TAG));
    }

    private void linkToTagsPage(CollectionModel<EntityModel<TagDto>> tagCollectionModel, int page, int size)
            throws ServiceException {
        tagCollectionModel.add(getLinkToTagsPage(page, size, ALL_TAGS));
    }

    private void linkToFirstTagsPage(EntityModel<TagDto> tagDto) throws ServiceException {
        tagDto.add(getLinkToTagsPage(TagRepresentationModelAssemblerImpl.DEFAULT_PAGE,
                TagRepresentationModelAssemblerImpl.DEFAULT_SIZE, ALL_TAGS));
    }

    private void linkToNextPage(CollectionModel<EntityModel<TagDto>> tagCollectionModel, int page, int size)
            throws ServiceException {
        tagCollectionModel.add(getLinkToTagsPage(page + 1, size, "next"));
    }

    private void linkToPrevPage(CollectionModel<EntityModel<TagDto>> tagCollectionModel, int page, int size)
            throws ServiceException {
        tagCollectionModel.add(getLinkToTagsPage(page - 1, size, "prev"));
    }

    private Link getLinkToTagsPage(int page, int size, String rel) throws ServiceException {
        return linkTo(methodOn(TagController.class).getTags(defaultRequestBody, page, size)).withRel(rel);
    }

    private boolean hasNext(int page, int lastPage) {
        return page < lastPage;
    }

    private boolean hasPrevious(int page) {
        return page > 1;
    }
}
