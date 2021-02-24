package com.epam.esm.web.api;

import com.epam.esm.model.Order;
import com.epam.esm.service.AuditedOrderService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.OrderSearchCriteria;
import com.epam.esm.service.search.criteria.UserSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import com.epam.esm.web.hateoas.pagination.impl.PaginationConfigurerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class UserController {

    private final UserService userService;
    private final AuditedOrderService auditedOrderService;
    private final OrderService orderService;
    private final ModelAssembler<UserDto> modelAssembler;
    private final ModelAssembler<OrderDto> orderModelAssembler;
    private final PaginationConfigurer paginationConfigurer;

    @Autowired
    public UserController(UserService userService, AuditedOrderService auditedOrderService,
                          ModelAssembler<UserDto> modelAssembler, ModelAssembler<OrderDto> orderModelAssembler,
                          OrderService orderService, PaginationValidator paginationValidator) {
        this.userService = userService;
        this.auditedOrderService = auditedOrderService;
        this.modelAssembler = modelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.orderService = orderService;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
        orderModelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasAuthority('users:read')")
    public CollectionModel<EntityModel<UserDto>> getUsers(
            @RequestBody(required = false) UserSearchCriteria request,
            @RequestParam int page, @RequestParam int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, userService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                UserDto.of(userService.getAllUsersByPage(request, page, size, sortType, sortBy))
        );
    }

    @GetMapping("/users/{id}")
    @PostAuthorize("(@userSecurity.hasUserId(authentication, #id) or hasAuthority('users:read'))")
    public EntityModel<UserDto> getUser(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(UserDto.of(userService.getUserById(id)));
    }

    @GetMapping("/users/{id}/orders")
    @PreAuthorize("(hasAuthority('orders:read') or @userSecurity.hasUserId(authentication, #id))")
    public CollectionModel<EntityModel<OrderDto>> getUserOrders(
            @RequestBody(required = false) OrderSearchCriteria requestBody,
            @RequestParam int page, @RequestParam int size, @PathVariable int id,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        return orderModelAssembler.toCollectionModel(
                OrderDto.of(auditedOrderService.getAuditedOrdersByUserId(id, requestBody, page, size, sortType, sortBy))
        );
    }

    @PostMapping("/users/{id}/orders")
    @PreAuthorize("hasAuthority('orders:write') and @userSecurity.hasUserId(authentication, #id)")
    public EntityModel<OrderDto> addUserOrder(@RequestBody Order order, @PathVariable int id)
            throws ServiceException {
        return orderModelAssembler.toModel(OrderDto.of(orderService.addUserOrder(order, id)));
    }

    @DeleteMapping("users/{id}/orders/{orderId}")
    @PreAuthorize(
            "hasAuthority('orders:write') and @userSecurity.orderHasUserId(authentication, #orderId) " +
                    "and @userSecurity.hasUserId(authentication, #id)"
    )
    public HttpStatus deleteOrder(@PathVariable int id, @PathVariable int orderId) throws ServiceException {
        orderService.deleteOrder(orderId);
        return HttpStatus.OK;
    }
}
