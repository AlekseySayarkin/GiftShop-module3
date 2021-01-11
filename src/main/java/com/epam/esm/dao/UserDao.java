package com.epam.esm.dao;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.model.User;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code User} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface UserDao {

    /**
     * Retrieves data of {@code User} from
     * data source by name
     * which equals to {@code String name}.
     *
     * @param login user login.
     * @return {@code User}.
     */
    User getUserByLogin(String login);

    /**
     * Retrieves data of {@code User} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id user id.
     * @return {@code User}.
     */
    User getUserById(int id);

    /**
     * Retrieves certain number of {@code User} from data source.
     *
     * @param requestBody object containing search criteria.
     * @param page from which position in a data source
     * to start returning.
     * @param size max amount of {@code Tag} to return.
     * @return List<Tag> - certain number of  existing tags in data source.
     */
    List<User> getAllUsersByPage(UserSearchCriteria requestBody, int page, int size);

    /**
     * Retrieves number of pages from data source if every page
     * contains certain number of {@code User}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size);
}
