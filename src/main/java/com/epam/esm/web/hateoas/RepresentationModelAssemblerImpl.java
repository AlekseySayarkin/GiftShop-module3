package com.epam.esm.web.hateoas;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RepresentationModelAssemblerImpl<T> implements ModelAssembler<T> {

    private ModelLinkBuilder<T> modelLinkBuilder;
    private RepresentationModel representationModel;

    @Override
    public void setModelLinkBuilder(ModelLinkBuilder<T> modelLinkBuilder) {
        this.modelLinkBuilder = modelLinkBuilder;
    }

    @Override
    public void setRepresentationModel(RepresentationModel representationModel) {
        this.representationModel = representationModel;
    }

    @Override
    public void addLinks(EntityModel<T> resource) {
        try {
            modelLinkBuilder.linkToModel(resource);
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<T>> resources) {
        if (representationModel != null) {
            PagedModel.PageMetadata metadata = representationModel.getPageMetadata();
            int size = (int) metadata.getSize();
            int page = (int) metadata.getNumber();
            int lastPage = (int) metadata.getTotalPages();
            SortType sortType = representationModel.getSortType();
            SortBy sortBy = representationModel.getSortBy();

            try {
                modelLinkBuilder.linkToModelPage(resources, page, size, sortType, sortBy);
                if (hasNext(page, lastPage)) {
                    modelLinkBuilder.linkToNextModelPage(resources, page, size, sortType, sortBy);
                    modelLinkBuilder.linkToLastModelPage(resources, lastPage, size, sortType, sortBy);
                }
                if (hasPrevious(page)) {
                    modelLinkBuilder.linkToPrevModelPage(resources, page, size, sortType, sortBy);
                }
            } catch (ServiceException ignored) {
            }
        }
    }

    private boolean hasNext(int page, int lastPage) {
        return page < lastPage;
    }

    private boolean hasPrevious(int page) {
        return page > 1;
    }
}
