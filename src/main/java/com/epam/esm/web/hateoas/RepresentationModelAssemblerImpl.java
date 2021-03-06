package com.epam.esm.web.hateoas;

import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
        modelLinkBuilder.linkToModel(resource);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<T>> resources) {
        if (representationModel != null) {
            var metadata = representationModel.getPageMetadata();
            int size = (int) metadata.getSize();
            int page = (int) metadata.getNumber();
            int lastPage = (int) metadata.getTotalPages();
            var sortType = representationModel.getSortType();
            var sortBy = representationModel.getSortBy();

            modelLinkBuilder.linkToModelPage(resources, page, size, sortType, sortBy);
            if (hasPrevious(page)) {
                modelLinkBuilder.linkToPrevModelPage(resources, page, size, sortType, sortBy);
            }
            if (resources.getContent().size() == size && hasNext(page, lastPage)) {
                modelLinkBuilder.linkToLastModelPage(resources, lastPage, size, sortType, sortBy);
                modelLinkBuilder.linkToNextModelPage(resources, page, size, sortType, sortBy);
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
