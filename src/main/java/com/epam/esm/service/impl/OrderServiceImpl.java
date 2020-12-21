package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.request.OrderRequestBody;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.OrderValidator;
import com.epam.esm.service.util.PaginationValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);
    private static final int MAX_PAGE_SIZE = 50;

    private final OrderDao orderDao;
    private final OrderValidator orderValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, OrderValidator orderValidator, PaginationValidator paginationValidator) {
        this.orderDao = orderDao;
        this.orderValidator = orderValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public List<Order> getTagByUserId(int id, OrderRequestBody requestBody, int page, int size) throws ServiceException {
        paginationValidator.validatePagination(size, page);

        if (requestBody == null) {
            requestBody = OrderRequestBody.getDefaultUserRequestBody();
        }
        if (!requestBody.getSortBy().equals(SortBy.COST)) {
            LOGGER.error("Cant sort orders by " + requestBody.getSortBy());
            throw new ServiceException("Cant sort orders by " + requestBody.getSortBy(),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }

        try {
            return orderDao.getTagByUserId(id, requestBody, page, size);
        } catch (DataAccessException e) {
            LOGGER.error(String.format("Failed to get order by user id = {%s}", id));
            throw new ServiceException(String.format("Failed to get order by user id = {%s}", id),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Order getOrder(int id) throws ServiceException {
        orderValidator.validateId(id);

        try {
            return orderDao.getOrder(id);
        } catch (DataAccessException e) {
            LOGGER.error(String.format("Failed to get order by user id = {%s}", id));
            throw new ServiceException(String.format("Failed to get order by user id = {%s}", id),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getMostFrequentTagFromHighestCostUser() throws ServiceException {
        try {
            return orderDao.getMostFrequentTagFromHighestCostUser();
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get most frequent tag");
            throw new ServiceException("Failed to get most frequent tag",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Order> getAllOrdersByPage(OrderRequestBody requestBody, int page, int size) throws ServiceException {
        paginationValidator.validatePagination(size, page);

        if (requestBody == null) {
            requestBody = OrderRequestBody.getDefaultUserRequestBody();
        }
        if (!requestBody.getSortBy().equals(SortBy.COST)) {
            LOGGER.error("Cant sort orders by " + requestBody.getSortBy());
            throw new ServiceException("Cant sort orders by " + requestBody.getSortBy(),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }

        try {
            return orderDao.getAllOrdersByPage(requestBody, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get orders by page");
            throw new ServiceException("Failed to get orders by page", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new ServiceException("Failed to get last page: size is negative",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }

        try {
            return orderDao.getLastPage(size);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Order addOrder(Order order) throws ServiceException {
        orderValidator.validateOrder(order);
        try {
            order.setId(orderDao.addOrder(order));
            return order;
        } catch (DataAccessException e) {
            LOGGER.error("Failed to add order");
            throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_ADD_ORDER);
        }
    }

    @Override
    public void deleteOrder(int orderId) throws ServiceException {
        orderValidator.validateId(orderId);
        try {
            if (!orderDao.deleteOrder(orderId)) {
                LOGGER.error("Failed to add order");
                throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_DELETE_ORDER);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Failed to add order");
            throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }
}
