package com.epam.esm.web.hateoas;

import com.epam.esm.dao.request.OrderRequestBody;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OrderLinkBuilder implements ModelLinkBuilder<OrderDto> {

    private static final String ALL_ORDERS = "orders";
    private static final String CURRENT_ORDER = "order";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final OrderRequestBody defaultRequestBody = OrderRequestBody.getDefaultUserRequestBody();

    @Override
    public void linkToModel(EntityModel<OrderDto> modelDto) throws ServiceException {
        modelDto.add(linkTo(methodOn(OrderController.class).getOrder(
                Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_ORDER));
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<OrderDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToOrdersPage(page, size, ALL_ORDERS));
    }

    @Override
    public void linkToFirstModelPage(EntityModel<OrderDto> tagDto) throws ServiceException {
        tagDto.add(getLinkToOrdersPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_ORDERS));
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<OrderDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToOrdersPage(page + 1, size, "next"));
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<OrderDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToOrdersPage(page - 1, size, "prev"));
    }

    private Link getLinkToOrdersPage(int page, int size, String rel) throws ServiceException {
        return linkTo(methodOn(OrderController.class).getOrders(defaultRequestBody, page, size)).withRel(rel);
    }
}
