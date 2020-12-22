package com.epam.esm.dao;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code Order} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface OrderDao {

    /**
     * Retrieves data of {@code Order} from
     * data source by name
     * which equals to {@code String name}.
     *
     * @param id userId.
     * @return {@code Order}.
     */
    List<Order> getTagByUserId(int id, OrderSearchCriteria requestBody, int page, int size)
            throws NoResultException;

    /**
     * Retrieves data of {@code Order} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id order id.
     * @return {@code Order}.
     */
    Order getOrderById(int id);

    /**
     * Get the most widely used tag of a user with the highest cost of all orders.
     *
     * @return {@link Tag}.
     */
    Tag getMostFrequentTagFromHighestCostUser();

    /**
     * Retrieves certain number of {@code Order} from data source.
     *
     * @param page page number of {@code Order} to return.
     * @param size page size of {@code Order} to return from data source.
     * @return List<Order> - certain number of existing orders in data source.
     */
    List<Order> getAllOrdersByPage(OrderSearchCriteria requestBody, int page, int size);

    /**
     * Retrieves number of pages from data source if every page
     * contains certain number of {@code Order}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size);

    /**
     * Adds new {@code Order} to data source.
     *
     * @param order {@code Order} which to be added to data source.
     * @return id of a {@code Order} from data source.
     */
    Order addOrder(Order order);

    /**
     * Deletes {@code Order} from data source.
     *
     * @param orderId id of a {@code Order} which to delete from data source.
     */
    void deleteOrder(int orderId);
}
