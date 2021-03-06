package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.TagSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with {@code Tag} in and out
 * of persistence layer.
 *
 * @author Aleksey Sayarkin
 */
public interface TagService {

    /**
     * Retrieves data of {@code Tag} from
     * persistence layer by it id
     * which equals to {@code int tagId}.
     *
     * @param tagId tag id.
     * @throws ServiceException when failed to get {@code Tag}.
     * @return {@code Tag}.
     */
    Tag getTagById(int tagId) throws ServiceException;

    /**
     * Retrieves {@code Tag} from persistence layer.
     *
     * @param searchCriteria object containing search criteria.
     * @param page from which position to start.
     * @param size max amount of {@code Tag} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code Tag}.
     * @return List<Tag> - tags from persistence layer.
     */
    List<Tag> getAllTagsByPage(TagSearchCriteria searchCriteria, int page, int size,
                               SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code Tag}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws ServiceException;

    /**
     * Adds new {@code Tag} to persistence layer.
     *
     * @param tag {@code Tag} which to be added to persistence layer.
     * @throws ServiceException when failed to add {@code Tag} to persistence layer.
     * @return id of a {@code Tag} from persistence layer.
     */
    Tag addTag(Tag tag) throws ServiceException;

    /**
     * Deletes {@code Tag} from persistence layer.
     *
     * @param tagId id of a {@code Tag} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code Tag} from persistence layer.
     */
    void deleteTag(int tagId) throws ServiceException;
}
