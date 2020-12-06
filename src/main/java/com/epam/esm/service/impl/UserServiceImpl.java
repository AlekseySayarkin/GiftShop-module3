package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
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
    public User getUser(String login) throws PersistenceException {
        userValidator.validateLogin(login);
        try {
            return userDao.getUser(login);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getUser(String login): " + e.getMessage());
            throw new PersistenceException("Failed to get user by it login: " + login,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public User getUser(int id) throws PersistenceException {
        userValidator.validateId(id);
        try {
            return userDao.getUser(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getUser(int id): " + e.getMessage());
            throw new PersistenceException("Failed to get user by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public List<User> getAllUsersByPage(int limit, int offset) throws PersistenceException {
        paginationValidator.validatePagination(limit, offset);
        try {
            return userDao.getAllUsersByPage(limit, offset);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllUsersByPage(): " + e.getMessage());
            throw new PersistenceException("Failed to get users",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }
}
