package com.epam.esm.web.controller;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import com.epam.esm.web.hateoas.pagination.impl.PaginationConfigurerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelAssembler<OrderDto> modelAssembler;
    private final PaginationConfigurer<OrderDto> paginationConfigurer;

    @Autowired
    public OrderController(OrderService orderService, ModelAssembler<OrderDto> modelAssembler,
                           PaginationValidator paginationValidator) {
        this.orderService = orderService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('orders:read')")
    public CollectionModel<EntityModel<OrderDto>> getOrders(
            @RequestBody(required = false) OrderSearchCriteria requestBody,
            @RequestParam int page, @RequestParam int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, orderService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                OrderDto.of(orderService.getAllOrdersByPage(requestBody, page, size, sortType, sortBy)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:read')")
    @PostAuthorize("@userSecurity.hasUserId(authentication, returnObject)")
    public EntityModel<OrderDto> getOrder(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(OrderDto.of(orderService.getOrderById(id)));
    }
}
