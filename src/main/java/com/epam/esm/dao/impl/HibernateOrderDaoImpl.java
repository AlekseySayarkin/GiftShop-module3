package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class HibernateOrderDaoImpl implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final PersistenceService<Order> persistenceService;

    private static final String GET_ORDER_BY_USER_ID = "SELECT o FROM Order o WHERE o.userId=:userId";
    private static final String GET_ORDER_COUNT = "SELECT count(o.id) FROM Order o";
    private static final String GET_MOST_FREQUENT_TAG =
            "SELECT tags.ID, tags.Name, count(tags.Name) AS count FROM Orders " +
            "INNER JOIN OrderDetails ON OrderDetails.OrderId = Orders.id " +
            "INNER JOIN GiftCertificates ON CertificateId = GiftCertificates.id " +
            "INNER JOIN CertificateDetails ON CertificateDetails.CertificateId = GiftCertificates.id " +
            "INNER JOIN tags on CertificateDetails.tagId = tags.id " +
            "WHERE userId IN ( " +
            "   SELECT userId FROM ( " +
            "       SELECT Sum(Cost) sumCost, userId " +
            "       FROM Orders " +
            "       GROUP BY userId " +
            "       order by sumCost desc " +
            "       LIMIT 1 " +
            "   ) AS ids " +
            ") " +
            "GROUP BY tags.ID " +
            "ORDER BY count DESC LIMIT 1";

    @Autowired
    public HibernateOrderDaoImpl(PersistenceService<Order> persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(Order.class);
    }

    @Override
    public List<Order> getTagByUserId(
            int id, OrderSearchCriteria requestBody, int page, int size) throws NoResultException {
        return persistenceService.getModelsByUserId(
                GET_ORDER_BY_USER_ID, id, page, size, requestBody.getSortType(), requestBody.getSortBy());
    }

    @Override
    public Order getOrderById(int id) {
        return persistenceService.getModelById(id);
    }

    @Override
    public Tag getMostFrequentTagFromHighestCostUser() {
        Query query = entityManager.createNativeQuery(
                GET_MOST_FREQUENT_TAG, Tag.class);
        return (Tag) query.getSingleResult();
    }

    @Override
    public List<Order> getAllOrdersByPage(OrderSearchCriteria requestBody, int page, int size) {
        return persistenceService.getAllModelsByPage(
                GET_ORDER_COUNT, page, size, requestBody.getSortType(), requestBody.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_ORDER_COUNT, size);
    }

    @Override
    public Order addOrder(Order order) {
        return persistenceService.addModel(order);
    }

    @Override
    public void deleteOrder(int orderId) {
        persistenceService.deleteModel(orderId);
    }
}
