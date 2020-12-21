package com.epam.esm.service;

import com.epam.esm.dao.request.OrderRequestBody;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface OrderService {

    /**
     * Retrieves data of {@code Order} from
     * persistence layer by name
     * which equals to {@code String name}.
     *
     * @param id userId.
     * @throws ServiceException when failed to get {@code Order} from persistence layer.
     * @return {@code Order}.
     */
    List<Order> getTagByUserId(int id, OrderRequestBody requestBody, int page, int size)
            throws ServiceException;

    /**
     * Retrieves data of {@code Order} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id order id.
     * @throws ServiceException when failed to get {@code Order} from persistence layer.
     * @return {@code Order}.
     */
    Order getOrder(int id) throws ServiceException;

    /**
     * Get the most widely used tag of a user with the highest cost of all orders.
     *
     * @return {@link Tag}.
     */
    Tag getMostFrequentTagFromHighestCostUser() throws ServiceException;

    /**
     * Retrieves certain number of {@code Order} from persistence layer.
     *
     * @param requestBody sort type and sort by.
     * @param page page of {@code Order} to return.
     * @param size amount of {@code Order} in one page.
     * @throws ServiceException when failed to get {@code Order} from persistence layer.
     * @return List<Order> - certain number of existing tags in persistence layer.
     */
    List<Order> getAllOrdersByPage(OrderRequestBody requestBody, int page, int size) throws ServiceException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code Order}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws ServiceException;

    /**
     * Adds new {@code Order} to persistence layer.
     *
     * @param order {@code Order} which to be added to persistence layer.
     * @throws ServiceException when failed to add {@code Order} to persistence layer.
     * @return newly created {@code Order} from persistence layer.
     */
    Order addOrder(Order order) throws ServiceException;

    /**
     * Deletes {@code Order} from persistence layer.
     *
     * @param orderId id of a {@code Order} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code Tag} from persistence layer.
     */
    void deleteOrder(int orderId) throws ServiceException;
}
