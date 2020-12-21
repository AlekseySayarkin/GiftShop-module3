package com.epam.esm.web.hateoas;

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
    private PagedModel.PageMetadata metadata;

    public void setModelLinkBuilder(ModelLinkBuilder<T> modelLinkBuilder) {
        this.modelLinkBuilder = modelLinkBuilder;
    }

    public void setMetadata(PagedModel.PageMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void addLinks(EntityModel<T> resource) {
        try {
            modelLinkBuilder.linkToModel(resource);
            modelLinkBuilder.linkToFirstModelPage(resource);
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<T>> resources) {
        if (metadata != null) {
            int size = (int) metadata.getSize();
            int page = (int) metadata.getNumber();
            int lastPage = (int) metadata.getTotalPages();

            try {
                modelLinkBuilder.linkToModelPage(resources, page, size);
                if (hasNext(page, lastPage)) {
                    modelLinkBuilder.linkToNextModelPage(resources, page, size);
                }
                if (hasPrevious(page)) {
                    modelLinkBuilder.linkToPrevModelPage(resources, page, size);
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
