package com.epam.esm.service.util;

import com.epam.esm.dao.exception.PersistenceException;

public interface PaginationValidator {

    void validatePagination(int limit, int offset) throws PersistenceException;
}
