package com.epam.esm.web.controller;

import com.epam.esm.dao.request.OrderRequestBody;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelAssembler<OrderDto> modelAssembler;

    @Autowired
    public OrderController(OrderService orderService, ModelAssembler<OrderDto> modelAssembler) {
        this.orderService = orderService;
        this.modelAssembler = modelAssembler;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping
    public CollectionModel<EntityModel<OrderDto>> getOrders(
            @RequestBody(required = false) OrderRequestBody requestBody,
            @RequestParam int page, @RequestParam int size) throws ServiceException {
        int lastPage = orderService.getLastPage(size);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                size, page, (long) lastPage * size, lastPage);
        modelAssembler.setMetadata(pageMetadata);

        return modelAssembler.toCollectionModel(
                OrderDto.of(orderService.getAllOrdersByPage(requestBody, page, size)));
    }

    @GetMapping("/{id}")
    public EntityModel<OrderDto> getOrder(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(OrderDto.of(orderService.getOrderById(id)));
    }

    @PostMapping
    public EntityModel<OrderDto> addOrder(@RequestBody Order order) throws ServiceException {
        return modelAssembler.toModel(OrderDto.of(orderService.addOrder(order)));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteOrder(@PathVariable int id) throws ServiceException {
        orderService.deleteOrder(id);
        return HttpStatus.OK;
    }
}
