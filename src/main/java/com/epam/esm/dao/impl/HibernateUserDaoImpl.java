package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

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
        removeDeletedOrdersFromUser(user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        User user =  persistenceService.getModelById(id);
        removeDeletedOrdersFromUser(user);
        return user;
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria requestBody, int page, int size) {
        List<User> users =  persistenceService.getAllModelsByPage(
                GET_ALL_USERS, page, size, requestBody.getSortType(), requestBody.getSortBy());
        removeDeletedOrdersFromUsers(users);

        return users;
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_USER_COUNT, size);
    }

    private void removeDeletedOrdersFromUsers(List<User> users) {
        users.forEach(this::removeDeletedOrdersFromUser);
    }

    private void removeDeletedOrdersFromUser(User user) {
        user.getOrders().removeIf(o -> !o.isActive());
    }
}
