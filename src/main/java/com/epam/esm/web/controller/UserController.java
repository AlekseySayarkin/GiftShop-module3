package com.epam.esm.web.controller;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
import com.epam.esm.web.hateoas.RepresentationModel;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final ModelAssembler<UserDto> modelAssembler;
    private final ModelAssembler<OrderDto> orderModelAssembler;

    @Autowired
    public UserController(UserService userService, ModelAssembler<UserDto> modelAssembler,
                          ModelAssembler<OrderDto> orderModelAssembler, OrderService orderService) {
        this.userService = userService;
        this.modelAssembler = modelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
        orderModelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping(value = "/users")
    public CollectionModel<EntityModel<UserDto>> getUsers(
            @RequestBody(required = false) UserSearchCriteria request,
            @RequestParam int page, @RequestParam int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        int lastPage = userService.getLastPage(size);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                size, page, (long) lastPage * size, lastPage);
        RepresentationModel model = new RepresentationModel(pageMetadata, sortType, sortBy);
        modelAssembler.setRepresentationModel(model);

        return modelAssembler.toCollectionModel(
                UserDto.of(userService.getAllUsersByPage(request, page, size, sortType, sortBy)));
    }

    @GetMapping("/users/{id}")
    public EntityModel<UserDto> getUser(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(UserDto.of(userService.getUserById(id)));
    }

    @GetMapping("/users/{id}/orders")
    public CollectionModel<EntityModel<OrderDto>> getUserOrders(
            @RequestBody(required = false) OrderSearchCriteria requestBody,
            @RequestParam int page, @RequestParam int size, @PathVariable int id,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        return orderModelAssembler.toCollectionModel(
                OrderDto.of(orderService.getOrdersByUserId(id, requestBody, page, size, sortType, sortBy)));
    }
}
