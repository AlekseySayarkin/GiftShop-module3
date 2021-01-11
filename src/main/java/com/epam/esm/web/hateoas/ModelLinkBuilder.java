package com.epam.esm.web.hateoas;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ModelLinkBuilder<T> {

    void linkToModel(EntityModel<T> modelDto) throws ServiceException;

    void linkToModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size,
                         SortType sortType, SortBy sortBy) throws ServiceException;

    void linkToFirstModelPage(EntityModel<T> tagDto, SortType sortType, SortBy sortBy) throws ServiceException;

    void linkToNextModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size,
                             SortType sortType, SortBy sortBy) throws ServiceException;

    void linkToPrevModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size,
                             SortType sortType, SortBy sortBy) throws ServiceException;

    void linkToLastModelPage(CollectionModel<EntityModel<T>> collectionModel, int lastPage, int size,
                             SortType sortType, SortBy sortBy) throws ServiceException;
}
