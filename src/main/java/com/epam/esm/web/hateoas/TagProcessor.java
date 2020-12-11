package com.epam.esm.web.hateoas;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.model.Tag;
import com.epam.esm.web.dto.TagDto;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface TagProcessor {

    PagedModel<Tag> setLinksToTagsPages(List<Tag> tags, int page, int size, int lastPage) throws DaoException;
    TagDto setLinksForTag(Tag tag) throws DaoException;
}
