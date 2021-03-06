package com.epam.esm.web.hateoas.pagination.impl;

import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.RepresentationModel;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import org.springframework.hateoas.PagedModel;

public class PaginationConfigurerImpl<T> implements PaginationConfigurer {

    private final ModelAssembler<T> modelAssembler;
    private final PaginationValidator paginationValidator;

    public PaginationConfigurerImpl(ModelAssembler<T> modelAssembler, PaginationValidator paginationValidator) {
        this.modelAssembler = modelAssembler;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public void configure(int page, int size, int lastPage, SortType sortType, SortBy sortBy)
            throws ServiceException {
        paginationValidator.validatePagination(page, size);
        var pageMetadata = new PagedModel.PageMetadata(size, page, (long) lastPage * size, lastPage);
        var model = new RepresentationModel(pageMetadata, sortType, sortBy);
        modelAssembler.setRepresentationModel(model);
    }
}
