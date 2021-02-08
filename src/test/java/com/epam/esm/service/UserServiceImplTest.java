package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.impl.HibernateUserDaoImpl;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import com.epam.esm.service.util.impl.UserValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImplTest {

    private UserDao userDao;
    private UserService userService;
    private int page;
    private int size;

    private User initUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("login");
        user.setPassword("password");

        return user;
    }

    private User initUser(int id) {
        User user = new User();
        user.setId(id);
        user.setLogin("login");
        user.setPassword("password");

        return user;
    }

    @BeforeEach
    public void init() {
        page = 1;
        size = 10;
        userDao = Mockito.mock(HibernateUserDaoImpl.class);

        userService = new UserServiceImpl(userDao,
                new UserValidatorImpl(), new PaginationValidatorImpl());
    }

    @Test
    void whenGetUser_thenCorrectlyReturnsItById() throws ServiceException {
        User given = initUser();

        Mockito.when(userDao.getUserById(given.getId())).thenReturn(given);

        User actual = userService.getUserById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(userDao).getUserById(given.getId());
    }

    @Test
    void whenGetUser_thenCorrectlyReturnsItByName() throws ServiceException {
        User given = initUser();

        Mockito.when(userDao.getUserByLogin(given.getLogin())).thenReturn(given);

        User actual = userService.getUserByLogin(given.getLogin());
        Assertions.assertEquals(given, actual);
        Mockito.verify(userDao).getUserByLogin(given.getLogin());
    }

    @Test
    void whenAddUser_thenCorrectlyReturnThem() throws ServiceException {
        List<User> given = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            given.add(initUser(i));
        }
        UserSearchCriteria givenSearchCriteria = UserSearchCriteria.getDefaultUserRequestBody();

        Mockito.when(userDao.getAllUsersByPage(givenSearchCriteria, size, page))
                .thenReturn(given);

        List<User> actual = userService.getAllUsersByPage(givenSearchCriteria, size, page,
                givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(given, actual);
        Mockito.verify(userDao).getAllUsersByPage(givenSearchCriteria, size, page);
    }
}