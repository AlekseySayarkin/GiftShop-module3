package com.epam.esm.service.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.AuditedOrderService;
import com.epam.esm.service.criteria.search.OrderSearchCriteria;
import com.epam.esm.service.criteria.sort.SortBy;
import com.epam.esm.service.criteria.sort.SortType;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.OrderValidator;
import com.epam.esm.service.util.PaginationUtil;
import com.epam.esm.service.util.PaginationValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.List;

@Service
public class AuditedOrderServiceImpl implements AuditedOrderService {

    private final static Logger log = LogManager.getLogger(AuditedOrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository certificateRepository;
    private final OrderValidator orderValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public AuditedOrderServiceImpl(OrderRepository orderRepository, GiftCertificateRepository certificateRepository,
                                  OrderValidator orderValidator, PaginationValidator paginationValidator) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.orderValidator = orderValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Order> getAuditedOrdersByUserId(int userId, OrderSearchCriteria searchCriteria, int page, int size,
                                                SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(page, size);

        if (searchCriteria == null) {
            searchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        orderValidator.validateOrderSearchCriteria(searchCriteria);

        try {
            var orders = orderRepository.findAll(
                    equalsToUserID(userId), PageRequest.of(--page, size, searchCriteria.getSort())
            ).getContent();
            orders.forEach(this::setRevisedCertificatesToOrder);

            return orders;
        } catch (DataAccessException e) {
            log.error("Failed to get order by user id = " + userId);
            throw new ServiceException("Failed to get order by user id = " + userId,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    private Specification<Order> equalsToUserID(int userId) {
        return (root, query, builder) -> {
            var user = root.join("user");
            return builder.equal(user.get("id"), userId);
        };
    }

    @Override
    public Order getAuditedOrderById(int orderId) throws ServiceException {
        orderValidator.validateId(orderId);

        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> {
                log.error("Failed to get order by user id = " + orderId);
                return new ServiceException(
                        "Failed to get order by user id = " + orderId, ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER
                );
            });

            setRevisedCertificatesToOrder(order);

            return order;
        } catch (DataAccessException | IllegalArgumentException e) {
            log.error("Failed to get order by user id = " + orderId);
            throw new ServiceException(
                    "Failed to get order by user id = " + orderId, ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER
            );
        }
    }

    private void setRevisedCertificatesToOrder(Order order) {
        var certificates = new HashSet<GiftCertificate>();
        order.getGiftCertificateList().forEach(c ->
            certificates.add(
                    certificateRepository
                            .findRevisions(c.getId())
                            .getContent()
                            .stream().findFirst()
                            .orElseThrow()
                            .getEntity()
            )

        );
        order.setGiftCertificateList(certificates);
    }

    @Override
    public List<Order> getAuditedOrdersByPage(OrderSearchCriteria searchCriteria, int page, int size,
                                              SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(page, size);

        if (searchCriteria == null) {
            searchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        orderValidator.validateOrderSearchCriteria(searchCriteria);

        try {
            return orderRepository.findAll(PageRequest.of(--page, size, searchCriteria.getSort())).getContent();
        } catch (DataAccessException e) {
            log.error("Failed to get orders by page");
            throw new ServiceException("Failed to get orders by page", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        paginationValidator.validateSize(size);
        try {
            return PaginationUtil.getLastPage((int) orderRepository.count(), size);
        } catch (DataAccessException | PersistenceException e) {
            log.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }
}
