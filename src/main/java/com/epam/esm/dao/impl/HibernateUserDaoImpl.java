package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Repository
public class HibernateUserDaoImpl implements UserDao {

    private final PersistenceService<User> persistenceService;

    private static final String GET_USER_BY_NAME = "SELECT u FROM User u WHERE u.login=:login ";
    private static final String GET_ALL_USERS = "SELECT u FROM User u ";
    private static final String GET_USER_COUNT = "SELECT count(u.id) FROM User u ";

    @Autowired
    public HibernateUserDaoImpl(PersistenceService<User> persistenceService) {
       this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(User.class);
    }

    @Override
    public User getUserByLogin(String login) {
        User user =  persistenceService.getModelByName(GET_USER_BY_NAME, login);
        removeDeletedOrders(user.getOrders());
        return user;
    }

    @Override
    public User getUserById(int id) {
        User user =  persistenceService.getModelById(id);
        removeDeletedOrders(user.getOrders());
        return user;
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria requestBody, int page, int size) {
        List<User> users =  persistenceService.getAllModelsByPage(
                GET_ALL_USERS, page, size, requestBody.getSortType(), requestBody.getSortBy());
        users.forEach(user -> removeDeletedOrders(user.getOrders()));

        return users;
    }

    private void removeDeletedOrders(Set<Order> orders) {
        orders.removeIf(o -> !o.isActive());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_USER_COUNT, size);
    }
}
