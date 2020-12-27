package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.service.util.UserValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private static final int MAX_PAGE_SIZE = 50;

    private final UserDao userDao;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator, PaginationValidator paginationValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public User getUserByLogin(String login) throws ServiceException {
        userValidator.validateLogin(login);
        try {
            return userDao.getUserByLogin(login);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getUser(String login): " + e.getMessage());
            throw new ServiceException("Failed to get user by it login: " + login,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public User getUserById(int id) throws ServiceException {
        userValidator.validateId(id);
        try {
            User user = userDao.getUserById(id);
            if (user == null) {
                throw new ServiceException("Failed to get user by it id: " + id, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
            }

            return user;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getUser(int id): " + e.getMessage());
            throw new ServiceException("Failed to get user by it id: " + id, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria requestBody, int limit, int offset) throws ServiceException {
        paginationValidator.validatePagination(limit, offset);

        if (requestBody == null) {
            requestBody = UserSearchCriteria.getDefaultUserRequestBody();
        }
        userValidator.validateUserSearchCriteria(requestBody);

        try {
            return userDao.getAllUsersByPage(requestBody, limit, offset);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllUsersByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get users", ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new ServiceException("Failed to get last page: size is negative",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
        return userDao.getLastPage(size);
    }
}
