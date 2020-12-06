package com.epam.esm.service.util.impl;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.User;
import com.epam.esm.service.util.UserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl implements UserValidator {

    @Override
    public void validateUser(User user) throws PersistenceException {
        if (user == null) {
            throw new PersistenceException("Failed to validate: user is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }

        validateId(user.getId());
        validateLogin(user.getLogin());
        validatePassword(user.getPassword());
    }

    @Override
    public void validateId(int id) throws PersistenceException {
        if (id < 0) {
            throw new PersistenceException("Failed to validate: id is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateLogin(String login) throws PersistenceException {
        if (login == null || login.isEmpty()) {
            throw new PersistenceException("Failed to validate: login is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    @Override
    public void validatePassword(String password) throws PersistenceException {
        if (password == null || password.isEmpty()) {
            throw new PersistenceException("Failed to validate: login is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }
}
