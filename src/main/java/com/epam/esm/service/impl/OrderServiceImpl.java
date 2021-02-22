package com.epam.esm.service.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.OrderValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

@Service
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderValidator orderValidator,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.userRepository = userRepository;
    }

    @Override
    public Tag getMostFrequentTagFromHighestCostUser() throws ServiceException {
        try {
            return orderRepository.getMostFrequentTagFromHighestCostUser();
        } catch (DataAccessException e) {
            log.error("Failed to get most frequent tag");
            throw new ServiceException("Failed to get most frequent tag", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Order addUserOrder(Order order, int userId) throws ServiceException {
        orderValidator.validateOrder(order);
        setUserToTheOrderByUserId(order, userId);
        try {
            order.setActive(true);
            return orderRepository.save(order);
        } catch (DataAccessException | PersistenceException e) {
            log.error("Failed to add order");
            throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_ADD_ORDER);
        }
    }

    private void setUserToTheOrderByUserId(Order order, int userId) throws ServiceException {
        var user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Failed to get user by it id: " + userId);
            return new ServiceException("Failed to get user by it id: " + userId, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        });
        order.setUser(user);
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteOrder(int orderId) throws ServiceException {
        orderValidator.validateId(orderId);
        try {
            orderRepository.deleteById(orderId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            log.error("Failed to delete order");
            throw new ServiceException("Failed to delete order", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }
}
