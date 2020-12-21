package com.epam.esm.service;

import com.epam.esm.dao.request.UserRequestBody;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;

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
     * @throws ServiceException when failed to get {@code User} from persistence layer.
     * @return {@code User}.
     */
    User getUserByLogin(String login) throws ServiceException;

    /**
     * Retrieves data of {@code User} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id user id.
     * @throws ServiceException when failed to get {@code User} from persistence layer.
     * @return {@code User}.
     */
    User getUserById(int id) throws ServiceException;

    /**
     * Retrieves certain number of {@code User} from persistence layer.
     *
     * @param limit max amount of {@code Tag} to return.
     * @param offset from which position in a persistence layer
     * to start returning.
     * @throws ServiceException when failed to get {@code User} from persistence layer.
     * @return List<Tag> - certain number of  existing tags in persistence layer.
     */
    List<User> getAllUsersByPage(UserRequestBody requestBody, int limit, int offset) throws ServiceException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code User}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws ServiceException;
}
