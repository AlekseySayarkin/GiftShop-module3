package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class HibernateOrderDaoImpl implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final boolean ACTIVE_ORDER = true;
    private static final boolean DELETED_ORDER = false;

    private final PersistenceService<Order> persistenceService;

    private static final String GET_ORDER_BY_USER_ID =
            "SELECT o FROM Order o WHERE o.user.id=:userId AND o.isActive=true ";
    private static final String GET_ALL_ORDERS = "SELECT o FROM Order o WHERE o.isActive=true ";
    private static final String GET_ORDER_COUNT = "SELECT count(o.id) FROM Order o WHERE o.isActive=true ";
    private static final String GET_MOST_FREQUENT_TAG =
            "SELECT tags.ID, tags.Name, count(tags.Name) AS count FROM Orders " +
            "INNER JOIN OrderCertificate ON OrderCertificate.OrderId = Orders.id " +
            "INNER JOIN GiftCertificates ON CertificateId = GiftCertificates.id " +
            "INNER JOIN CertificateTag ON CertificateTag.CertificateId = GiftCertificates.id " +
            "INNER JOIN tags on CertificateTag.tagId = tags.id " +
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
    public List<Order> getOrdersByUserId(
            int userId, OrderSearchCriteria requestBody, int page, int size) throws NoResultException {
        List<Order> orders = getNotAuditedOrdersByUserId(userId, requestBody, page, size);
        List<GiftCertificate> giftCertificateList = getFirstVersionOfOrders(orders);

        for (Order order: orders) {
            List<GiftCertificate> temp = new ArrayList<>(order.getGiftCertificateList());
            for (GiftCertificate giftCertificate: giftCertificateList) {
                temp.replaceAll(t -> t.getId() == giftCertificate.getId() ? giftCertificate : t);
            }
            order.setGiftCertificateList(new HashSet<>(temp));
        }

        return orders;
    }

    private List<Order> getNotAuditedOrdersByUserId(int id, OrderSearchCriteria requestBody, int page, int size) {
        String query = GET_ORDER_BY_USER_ID +
                "order by " + requestBody.getSortBy() + " " + requestBody.getSortType();
        TypedQuery<Order> typedQuery = entityManager.createQuery(query, Order.class);
        typedQuery.setParameter("userId", id);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return  typedQuery.getResultList();
    }

    private List<GiftCertificate> getFirstVersionOfOrders(List<Order> orders) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager)
                .createQuery().forRevisionsOfEntity(GiftCertificate.class, true, true);
        auditQuery.add(AuditEntity.revisionNumber().minimize().computeAggregationInInstanceContext());

        for (Order order: orders) {
            for (GiftCertificate giftCertificate: order.getGiftCertificateList()) {
                auditQuery.add(AuditEntity.id().eq(giftCertificate.getId()));
            }
        }

        return auditQuery.getResultList();
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
                GET_ALL_ORDERS, page, size, requestBody.getSortType(), requestBody.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_ORDER_COUNT, size);
    }

    @Override
    public Order addOrder(Order order) {
        order.setActive(ACTIVE_ORDER);
        return persistenceService.add(order);
    }

    @Override
    public void deleteOrder(int orderId) {
        Order order = persistenceService.getModelById(orderId);
        if (order == null) {
            throw new NoResultException("Failed to find order to delete by id:" + orderId);
        }
        order.setActive(DELETED_ORDER);
        persistenceService.update(order);
    }

    @Transactional
    public void updateMethodToBeDeleted(Order order) {
        entityManager.merge(order);
        entityManager.flush();
    }
}
