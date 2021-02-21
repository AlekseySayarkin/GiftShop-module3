package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

/**
 * This interface provides with ability to create
 * transactions with {@code Order} in and out
 * of persistence layer.
 *
 * @author Aleksey Sayarkin
 */
public interface OrderService {

    /**
     * Get the most widely used tag of a user with the highest cost of all orders.
     *
     * @return {@link Tag}.
     */
    Tag getMostFrequentTagFromHighestCostUser() throws ServiceException;

    /**
     * Adds new {@code Order} to persistence layer.
     *
     * @param order {@code Order} which to add to persistence layer.
     * @param userId id of a user, to whom order belongs.
     * @throws ServiceException when failed to add {@code Order}.
     * @return added {@code Order} from persistence layer.
     */
    Order addUserOrder(Order order, int userId) throws ServiceException;

    /**
     * Deletes {@code Order} from persistence layer.
     *
     * @param orderId id of {@code Order} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code Order}.
     */
    void deleteOrder(int orderId) throws ServiceException;
}
