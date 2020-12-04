package com.epam.esm.service.util;

import com.epam.esm.model.Tag;
import com.epam.esm.dao.exception.PersistenceException;

public interface TagValidator {

    void validateTag(Tag tag) throws PersistenceException;
    void validateId(int id) throws PersistenceException;
    void validateName(String name) throws PersistenceException;
}
