package com.epam.esm.web.hateoas;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.web.dto.TagDto;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

public interface TagProcessor {

    CollectionModel<Tag> processGetTags(List<Tag> tags, TagRequestBody request) throws PersistenceException;
    TagDto processGetTag(Tag tag) throws PersistenceException;
    TagDto processAddTag(Tag tag) throws PersistenceException;
}
