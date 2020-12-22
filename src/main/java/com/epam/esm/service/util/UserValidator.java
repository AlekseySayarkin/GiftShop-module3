package com.epam.esm.service.util;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;

public interface UserValidator {

    void validateUser(User user) throws ServiceException;
    void validateId(int id) throws ServiceException;
    void validateLogin(String login) throws ServiceException;
    void validatePassword(String password) throws ServiceException;
    void validateUserSearchCriteria(UserSearchCriteria searchCriteria) throws ServiceException;
}
