package com.epam.esm.web.api;

import com.epam.esm.service.AuditedOrderService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.OrderSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
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

    private final AuditedOrderService auditedOrderService;
    private final ModelAssembler<OrderDto> modelAssembler;
    private final PaginationConfigurer paginationConfigurer;

    @Autowired
    public OrderController(ModelAssembler<OrderDto> modelAssembler, PaginationValidator paginationValidator,
                           AuditedOrderService auditedOrderService) {
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
        this.auditedOrderService = auditedOrderService;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('orders:read') and #oauth2.hasScope('read')")
    public CollectionModel<EntityModel<OrderDto>> getOrders(
            @RequestBody(required = false) OrderSearchCriteria requestBody,
            @RequestParam int page, @RequestParam int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, auditedOrderService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                OrderDto.of(auditedOrderService.getAuditedOrdersByPage(requestBody, page, size, sortType, sortBy))
        );
    }

    @GetMapping("/{id}")
    @PostAuthorize(
            "(hasAuthority('orders:read') or @userSecurity.hasUserId(authentication, returnObject))" +
                    " and #oauth2.hasScope('read')"
    )
    public EntityModel<OrderDto> getOrder(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(OrderDto.of(auditedOrderService.getAuditedOrderById(id)));
    }
}
