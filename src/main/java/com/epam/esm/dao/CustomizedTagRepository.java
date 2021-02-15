package com.epam.esm.dao;

import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.model.Tag;

import java.util.List;

public interface CustomizedTagRepository {

    /**
     * Retrieves certain number of {@code Tag} from data source
     * filtered by {@code TagSearchCriteria}.
     *
     * @param searchCriteria object containing search criteria.
     * @param page page number of {@code Tag} to return.
     * @param size page size of {@code Tag} to return from data source.
     * @return List<Tag> - certain number of tags in data source.
     */
    List<Tag> getAllTagsByPage(TagSearchCriteria searchCriteria, int page, int size);

    void deleteById(int tagId);
}
