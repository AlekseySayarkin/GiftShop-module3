package com.epam.esm.service.util;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.model.User;

public interface UserValidator {

    void validateUser(User user) throws DaoException;
    void validateId(int id) throws DaoException;
    void validateLogin(String login) throws DaoException;
    void validatePassword(String password) throws DaoException;
}
