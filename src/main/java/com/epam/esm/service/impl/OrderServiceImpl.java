package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Order> getOrdersByUserId(int id, OrderSearchCriteria requestBody, int page, int size,
                                         SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(size, page);

        if (requestBody == null) {
            requestBody = OrderSearchCriteria.getDefaultUserRequestBody();
        }
        requestBody.setSortType(sortType);
        requestBody.setSortBy(sortBy);
        orderValidator.validateOrderSearchCriteria(requestBody);

        try {
            return orderDao.getOrdersByUserId(id, requestBody, page, size);
        } catch (DataAccessException e) {
            LOGGER.error(String.format("Failed to get order by user id = {%s}", id));
            throw new ServiceException(String.format("Failed to get order by user id = {%s}", id),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public Order getOrderById(int id) throws ServiceException {
        orderValidator.validateId(id);

        try {
            Order order = orderDao.getOrderById(id);
            if (order == null) {
                throw new ServiceException(String.format("Failed to get order by user id = {%s}", id),
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
            }

            return order;
        } catch (DataAccessException e) {
            LOGGER.error(String.format("Failed to get order by user id = {%s}", id));
            throw new ServiceException(String.format("Failed to get order by user id = {%s}", id),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public Tag getMostFrequentTagFromHighestCostUser() throws ServiceException {
        try {
            return orderDao.getMostFrequentTagFromHighestCostUser();
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get most frequent tag");
            throw new ServiceException("Failed to get most frequent tag",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public List<Order> getAllOrdersByPage(OrderSearchCriteria requestBody, int page, int size,
                                          SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(size, page);

        if (requestBody == null) {
            requestBody = OrderSearchCriteria.getDefaultUserRequestBody();
        }
        requestBody.setSortType(sortType);
        requestBody.setSortBy(sortBy);
        orderValidator.validateOrderSearchCriteria(requestBody);

        try {
            return orderDao.getAllOrdersByPage(requestBody, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get orders by page");
            throw new ServiceException("Failed to get orders by page", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
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
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Order addOrder(Order order) throws ServiceException {
        orderValidator.validateOrder(order);
        try {
            return orderDao.addOrder(order);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to add order");
            throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_ADD_ORDER);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteOrder(int orderId) throws ServiceException {
        orderValidator.validateId(orderId);
        try {
            orderDao.deleteOrder(orderId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Failed to delete order");
            throw new ServiceException("Failed to delete order", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }
}
