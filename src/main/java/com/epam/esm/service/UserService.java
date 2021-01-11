package com.epam.esm.service;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
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
     * @param requestBody object containing search criteria.
     * @param page from which position in a data source.
     * @param size max amount of {@code GiftCertificate} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * to start returning.
     * @throws ServiceException when failed to get {@code User} from persistence layer.
     * @return List<Tag> - certain number of  existing tags in persistence layer.
     */
    List<User> getAllUsersByPage(UserSearchCriteria requestBody, int page, int size,
                                 SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code User}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws ServiceException;
}
