package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.request.TagRequestBody;

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
     * persistence layer by name
     * which equals to {@code String name}.
     *
     * @param name tag name.
     * @throws DaoException when failed to get {@code Tag} from persistence layer.
     * @return {@code Tag}.
     */
    Tag getTag(String name) throws DaoException;

    /**
     * Retrieves data of {@code Tag} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id tag id.
     * @throws DaoException when failed to get {@code Tag} from persistence layer.
     * @return {@code Tag}.
     */
    Tag getTag(int id) throws DaoException;

    /**
     * Retrieves all {@code Tag} from persistence layer.
     *
     * @throws DaoException when failed to get {@code Tag} from persistence layer.
     * @return List<Tag> - all existing tags in persistence layer.
     */
    List<Tag> getAllTags() throws DaoException;

    /**
     * Retrieves certain number of {@code Tag} from persistence layer.
     *
     * @param requestBody sort type and sort by.
     * @param page page of {@code Tag} to return.
     * @param size amount of {@code Tag} in one page.
     * @throws DaoException when failed to get {@code Tag} from persistence layer.
     * @return List<Tag> - certain number of existing tags in persistence layer.
     */
    List<Tag> getAllTagsByPage(TagRequestBody requestBody, int page, int size) throws DaoException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code Tag}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws DaoException;

    /**
     * Adds new {@code Tag} to persistence layer.
     *
     * @param tag {@code Tag} which to be added to persistence layer.
     * @throws DaoException when failed to add {@code Tag} to persistence layer.
     * @return id of a {@code Tag} from persistence layer.
     */
    Tag addTag(Tag tag) throws DaoException;

    /**
     * Deletes {@code Tag} from persistence layer.
     *
     * @param tagId id of a {@code Tag} which to delete from persistence layer.
     * @throws DaoException when failed to delete {@code Tag} from persistence layer.
     */
    void deleteTag(int tagId) throws DaoException;
}
