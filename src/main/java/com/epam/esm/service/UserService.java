package com.epam.esm.service;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.model.User;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with {@code User} in and out
 * of persistence layer.
 *
 * @author Aleksey Sayarkin
 */
public interface UserService {

    /**
     * Retrieves data of {@code User} from
     * persistence layer by name
     * which equals to {@code String name}.
     *
     * @param login user login.
     * @throws DaoException when failed to get {@code User} from persistence layer.
     * @return {@code User}.
     */
    User getUser(String login) throws DaoException;

    /**
     * Retrieves data of {@code User} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id user id.
     * @throws DaoException when failed to get {@code User} from persistence layer.
     * @return {@code User}.
     */
    User getUser(int id) throws DaoException;

    /**
     * Retrieves certain number of {@code User} from persistence layer.
     *
     * @param limit max amount of {@code Tag} to return.
     * @param offset from which position in a persistence layer
     * to start returning.
     * @throws DaoException when failed to get {@code User} from persistence layer.
     * @return List<Tag> - certain number of  existing tags in persistence layer.
     */
    List<User> getAllUsersByPage(int limit, int offset) throws DaoException;
}
