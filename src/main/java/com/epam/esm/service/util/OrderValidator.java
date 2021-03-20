package com.epam.esm.service.util;

import com.epam.esm.model.Order;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.OrderSearchCriteria;

/**
 * This interface provides with ability to validate
 * given {@code Order} or it specific field.
 *
 * @author Aleksey Sayarkin
 */
public interface OrderValidator {

    /**
     * Validates {@code Order}.
     *
     * @param order {@code Order} to be validated.
     * @throws ServiceException if {@code Order} is incorrect.
     */
    void validateOrder(Order order) throws ServiceException;

    /**
     * Validates id of {@code Order}.
     *
     * @param id id of {@code Order} to be validated.
     * @throws ServiceException if id of {@code Order} is incorrect.
     */
    void validateId(int id) throws ServiceException;

    /**
     * Validates searchCriteria of {@code Order}.
     *
     * @param searchCriteria searchCriteria of {@code Order} to be validated.
     * @throws ServiceException if searchCriteria of {@code Order} is incorrect.
     */
    void validateOrderSearchCriteria(OrderSearchCriteria searchCriteria) throws ServiceException;
}
