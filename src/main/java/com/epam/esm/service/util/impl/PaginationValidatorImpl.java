package com.epam.esm.service.util.impl;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.service.util.PaginationValidator;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidatorImpl implements PaginationValidator {

    private static final int MAX_AMOUNT_OF_ITEMS = 100;

    @Override
    public void validatePagination(int limit, int offset) throws PersistenceException {
        if (limit <= 0) {
            throw new PersistenceException("Failed to validate: limit is negative",
                    ErrorCodeEnum.PAGINATION_VALIDATION_ERROR);
        }
        if (limit > MAX_AMOUNT_OF_ITEMS) {
            throw new PersistenceException("Failed to validate: limit is bigger then maximum allowed",
                    ErrorCodeEnum.PAGINATION_VALIDATION_ERROR);
        }
        if (offset< 0) {
            throw new PersistenceException("Failed to validate: offset is negative",
                    ErrorCodeEnum.PAGINATION_VALIDATION_ERROR);
        }
    }
}
