package com.epam.esm.service.util.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    @Override
    public void validateOrder(Order order) throws ServiceException {
        if (order == null) {
            throw new ServiceException("Failed to validate: order is empty",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }

        validateId(order.getId());
        validateCost(order.getCost());
        validateUser(order.getUser());
    }

    @Override
    public void validateId(int id) throws ServiceException {
        if (id < 0) {
            throw new ServiceException("Failed to validate: order id is negative",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }

    private void validateUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("Failed to validate: user is empty",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }

        if (user.getId() <= 0) {
            throw new ServiceException("Failed to validate: user id is negative",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }

    private void validateCost(double cost) throws ServiceException {
        if (cost <= 0) {
            throw new ServiceException("Failed to validate: cost must be positive",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }
}
