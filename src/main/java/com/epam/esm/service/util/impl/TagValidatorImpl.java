package com.epam.esm.service.util.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.TagSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.util.TagValidator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements TagValidator {

    @Override
    public void validateTag(Tag tag) throws ServiceException {
        if (tag == null) {
            throw new ServiceException("Failed to validate: tag is empty", ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
        validateId(tag.getId());
        validateName(tag.getName());
    }

    @Override
    public void validateId(int id) throws ServiceException {
        if (id < 0) {
            throw new ServiceException("Failed to validate: tag id is negative", ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateName(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Failed to validate: tag name is empty", ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateTagSearchCriteria(TagSearchCriteria searchCriteria) throws ServiceException {
        if (!searchCriteria.getSortBy().equals(SortBy.NAME)) {
            throw new ServiceException(
                    "Cant sort tags by " + searchCriteria.getSortBy(), ErrorCodeEnum.TAG_VALIDATION_ERROR
            );
        }
    }
}
