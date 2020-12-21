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

    private static final String SQL_GET_USER_BY_NAME = "SELECT u FROM u WHERE u.login=:login";
    private static final String SQL_GET_ALL_USERS = "SELECT u FROM u";
    private static final String GET_USER_COUNT = "SELECT count(u.id) FROM User u";

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
        return  persistenceService.getModelByName(SQL_GET_USER_BY_NAME, login);
    }

    @Override
    public User getUserById(int id) {
        return persistenceService.getModelById(id);
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria requestBody, int page, int size) {
        return persistenceService.getAllModelsByPage(
                SQL_GET_ALL_USERS, page, size, requestBody.getSortType(), requestBody.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_USER_COUNT, size);
    }
}
