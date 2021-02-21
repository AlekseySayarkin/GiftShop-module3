package com.epam.esm.service;

import com.epam.esm.service.criteria.search.UserSearchCriteria;
import com.epam.esm.service.criteria.sort.SortBy;
import com.epam.esm.service.criteria.sort.SortType;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
     * persistence layer by it login
     * which equals to {@code String login}.
     *
     * @param login user login.
     * @throws ServiceException when failed to get {@code User}.
     * @return {@code User}.
     */
    User getUserByLogin(String login) throws ServiceException;

    /**
     * Retrieves data of {@code User} from
     * persistence layer by it id
     * which equals to {@code int userId}.
     *
     * @param userId user id.
     * @throws ServiceException when failed to get {@code User}.
     * @return {@code User}.
     */
    User getUserById(int userId) throws ServiceException;

    /**
     * Retrieves {@code User} from persistence layer.
     *
     * @param searchCriteria object containing search criteria.
     * @param page from which position to start.
     * @param size max amount of {@code User} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code User}.
     * @return List<User> - users from persistence layer.
     */
    List<User> getAllUsersByPage(UserSearchCriteria searchCriteria, int page, int size,
                                 SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code User}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws ServiceException;

    /**
     * Adds new {@code User} to data source.
     *
     * @param user {@code User} which to be added to data source.
     * @throws ServiceException when failed to add {@code User}.
     * @return added {@code User} from data source.
     */
    User addUser(User user)throws ServiceException;

    /**
     * Retrieves user or if not found adds new {@code User} to data source.
     *
     * @param user {@code OAuth2User} which to be retrieved or added to data source.
     * @throws ServiceException when failed to add {@code User}.
     * @return retrieved or added {@code User} from data source.
     */
    User getOrAddByLogin(OAuth2User user) throws ServiceException;
}
