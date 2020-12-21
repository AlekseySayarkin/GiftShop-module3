package com.epam.esm.service.util;

import com.epam.esm.service.exception.ServiceException;

public interface PaginationValidator {

    void validatePagination(int limit, int offset) throws ServiceException;
}
