package com.epam.esm.dao;

import com.epam.esm.dao.request.TagRequestBody;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.model.Tag;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code Tag} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface TagDao {

    /**
     * Retrieves data of {@code Tag} from
     * data source by name
     * which equals to {@code String name}.
     *
     * @param name tag name.
     * @return {@code Tag}.
     */
    Tag getTagByName(String name) throws NoResultException;

    /**
     * Retrieves data of {@code Tag} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id tag id.
     * @return {@code Tag}.
     */
    Tag getTagById(int id);

    /**
     * Retrieves all {@code Tag} from data source.
     *
     * @return List<Tag> - all existing tags in data source.
     */
    List<Tag> getAllTags();

    /**
     * Retrieves certain number of {@code Tag} from data source.
     *
     * @param page page number of {@code Tag} to return.
     * @param size page size of {@code Tag} to return from data source.
     * @return List<Tag> - certain number of  existing tags in data source.
     */
    List<Tag> getAllTagsByPage(TagRequestBody requestBody, int page, int size);

    /**
     * Retrieves number of pages from data source if every page
     * contains certain number of {@code Tag}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size);

    /**
     * Adds new {@code Tag} to data source.
     *
     * @param tag {@code Tag} which to be added to data source.
     * @return id of a {@code Tag} from data source.
     * @throws ServiceException when failed to add {@code Tag} to data source.
     */
    int addTag(Tag tag) throws ServiceException;

    /**
     * Deletes {@code Tag} from data source.
     *
     * @param tagId id of a {@code Tag} which to delete from data source.
     * @return whether transaction was successful.
     */
    boolean deleteTag(int tagId);
}
