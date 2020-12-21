package com.epam.esm.web.hateoas;

import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

public interface ModelAssembler<T> extends SimpleRepresentationModelAssembler<T> {

    void setMetadata(PagedModel.PageMetadata metadata);
    void setModelLinkBuilder(ModelLinkBuilder<T> modelLinkBuilder);
}
