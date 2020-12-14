package com.epam.esm.dao;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.model.Order;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code Order} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface OrderDao {

    Order getOrder(int id) throws DaoException;
    List<Order> getOrdersByPage(int limit, int offset) throws DaoException;
    List<Order> getOrdersByUserId(int userId, int limit, int offset) throws DaoException;
    int addOrder(Order order) throws DaoException;
    boolean deleteOrder(int id) throws DaoException;
    boolean updateOrder(Order order) throws DaoException;
}
