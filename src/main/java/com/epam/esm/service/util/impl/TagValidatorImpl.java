package com.epam.esm.service.util.impl;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.service.util.TagValidator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements TagValidator {

    @Override
    public void validateTag(Tag tag) throws PersistenceException {
        if (tag == null) {
            throw new PersistenceException("Failed to validate: tag is empty", ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
        validateId(tag.getId());
        validateName(tag.getName());
    }

    public void validateId(int id) throws PersistenceException {
        if (id < 0) {
            throw new PersistenceException("Failed to validate: tag id is negative",
                    ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }

    public void validateName(String name) throws PersistenceException {
        if (name == null || name.isEmpty()) {
            throw new PersistenceException("Failed to validate: tag name is empty",
                    ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }
}
