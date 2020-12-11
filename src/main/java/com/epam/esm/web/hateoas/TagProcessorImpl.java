package com.epam.esm.web.hateoas;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.model.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagProcessorImpl implements TagProcessor {

    private static final String ALL_TAGS = "tags";
    private static final String CURRENT_TAG = "tag";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final TagRequestBody defaultRequestBody = TagRequestBody.getDefaultTagRequestBody();

    @Override
    public PagedModel<Tag> setLinksToTagsPages(List<Tag> tags, int page, int size, int lastPage)
            throws DaoException {
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page,
                (long) lastPage * size, lastPage);
        PagedModel<Tag> pagedModel = PagedModel.of(tags, pageMetadata);

        linkToTagsPage(pagedModel, page, size);
        if (hasNext(page, lastPage)) {
            linkToNextPage(pagedModel, page, size);
        }
        if (hasPrevious(page)) {
            linkToPrevPage(pagedModel, page, size);
        }

        return pagedModel;
    }

    @Override
    public TagDto setLinksForTag(Tag tag) throws DaoException {
        TagDto tagDto = TagDto.of(tag);
        linkToTag(tagDto);
        linkToFirstTagsPage(tagDto);

        return tagDto;
    }

    private void linkToTag(TagDto tagDto) throws DaoException {
        tagDto.add(linkTo(methodOn(TagController.class).getTag(tagDto.getId())).withRel(CURRENT_TAG));
    }

    private void linkToTagsPage(CollectionModel<Tag> tagCollectionModel, int page, int size)
            throws DaoException {
        tagCollectionModel.add(getLinkToTagsPage(page, size, ALL_TAGS));
    }

    private void linkToFirstTagsPage(TagDto tagDto) throws DaoException {
        tagDto.add(getLinkToTagsPage(TagProcessorImpl.DEFAULT_PAGE, TagProcessorImpl.DEFAULT_SIZE, ALL_TAGS));
    }

    private void linkToNextPage(CollectionModel<Tag> tagCollectionModel, int page, int size)
            throws DaoException {
        tagCollectionModel.add(getLinkToTagsPage(page + 1, size, "next"));
    }

    private void linkToPrevPage(CollectionModel<Tag> tagCollectionModel, int page, int size)
            throws DaoException {
        tagCollectionModel.add(getLinkToTagsPage(page - 1, size, "prev"));
    }

    private Link getLinkToTagsPage(int page, int size, String rel) throws DaoException {
        return linkTo(methodOn(TagController.class).getTags(defaultRequestBody, page, size)).withRel(rel);
    }

    private boolean hasNext(int page, int lastPage) {
        return page < lastPage;
    }

    private boolean hasPrevious(int page) {
        return page > 1;
    }
}
