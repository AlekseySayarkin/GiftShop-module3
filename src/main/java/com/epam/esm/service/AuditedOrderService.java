package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.service.criteria.search.OrderSearchCriteria;
import com.epam.esm.service.criteria.sort.SortBy;
import com.epam.esm.service.criteria.sort.SortType;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface AuditedOrderService {

    /**
     * Retrieves {@code Order} from persistence layer.
     *
     * @param userId id of a user which orders to retrieve.
     * @param searchCriteria object containing search criteria.
     * @param page from which position in a data source.
     * @param size max amount of {@code Order} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code Order}.
     * @return List<Order> - orders from persistence layer.
     */
    List<Order> getAuditedOrdersByUserId(int userId, OrderSearchCriteria searchCriteria, int page, int size,
                                         SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves data of {@code Order} from
     * persistence layer by it id
     * which equals to {@code int orderId}.
     *
     * @param orderId order id.
     * @throws ServiceException when failed to get {@code Order}.
     * @return {@code Order}.
     */
    Order getAuditedOrderById(int orderId) throws ServiceException;

    /**
     * Retrieves {@code Order} from persistence layer.
     *
     * @param searchCriteria object containing search criteria.
     * @param page from which position to start.
     * @param size max amount of {@code GiftCertificate} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code Order}.
     * @return List<Order> - orders from persistence layer.
     */
    List<Order> getAuditedOrdersByPage(OrderSearchCriteria searchCriteria, int page, int size,
                                       SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves number of a last page from persistence layer if every page
     * contains certain number of {@code Order}.
     *
     * @param size size of a page.
     * @return number of a last page.
     */
    int getLastPage(int size) throws ServiceException;
}
