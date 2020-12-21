package com.epam.esm.service.util;

import com.epam.esm.model.Order;
import com.epam.esm.service.exception.ServiceException;

public interface OrderValidator {

    void validateOrder(Order order) throws ServiceException;
    void validateId(int id) throws ServiceException;
}
