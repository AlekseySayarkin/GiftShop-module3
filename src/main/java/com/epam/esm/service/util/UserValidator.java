package com.epam.esm.service.util;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.User;

public interface UserValidator {

    void validateUser(User user) throws PersistenceException;
    void validateId(int id) throws PersistenceException;
    void validateLogin(String login) throws PersistenceException;
    void validatePassword(String password) throws PersistenceException;
}
