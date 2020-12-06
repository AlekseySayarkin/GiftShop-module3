package com.epam.esm.dao;

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
    User getUser(String login);

    /**
     * Retrieves data of {@code User} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id user id.
     * @return {@code User}.
     */
    User getUser(int id);

    /**
     * Retrieves certain number of {@code User} from data source.
     *
     * @param limit max amount of {@code Tag} to return.
     * @param offset from which position in a data source
     * to start returning.
     * @return List<Tag> - certain number of  existing tags in data source.
     */
    List<User> getAllUsersByPage(int limit, int offset);
}
