package com.epam.esm.web.hateoas;

import com.epam.esm.service.exception.ServiceException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ModelLinkBuilder<T> {

    void linkToModel(EntityModel<T> modelDto) throws ServiceException;

    void linkToModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size)
            throws ServiceException;

    void linkToFirstModelPage(EntityModel<T> tagDto) throws ServiceException;

    void linkToNextModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size)
            throws ServiceException;

    void linkToPrevModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size)
            throws ServiceException;
}
