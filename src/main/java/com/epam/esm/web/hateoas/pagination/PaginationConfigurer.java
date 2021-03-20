package com.epam.esm.web.hateoas.pagination;

import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;

public interface PaginationConfigurer {

    void configure(int page, int size, int lastPage, SortType sortType, SortBy sortBy) throws ServiceException;
}
