package com.epam.esm.service.util;

import com.epam.esm.dao.exception.DaoException;

public interface PaginationValidator {

    void validatePagination(int limit, int offset) throws DaoException;
}
