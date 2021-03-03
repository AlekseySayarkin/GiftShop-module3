package com.epam.esm.service;

import com.epam.esm.model.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.AuditedOrderServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.search.criteria.UserSearchCriteria;
import com.epam.esm.service.util.impl.OrderValidatorImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import com.epam.esm.service.util.impl.UserValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GiftCertificateRepository certificateRepository;

    private UserServiceImpl userService;

    private final static int PAGE = 1;
    private final static int SIZE = 10;

    private User initUser() {
        var user = new User();
        user.setId(1);
        user.setLogin("login");
        user.setPassword("password");

        return user;
    }

    private User initUser(int id) {
        var user = new User();
        user.setId(id);
        user.setLogin("login");
        user.setPassword("password");

        return user;
    }

    @BeforeEach
    public void setUp() {
        var userValidation = new UserValidatorImpl();
        var orderValidator =  new OrderValidatorImpl();
        var paginationValidator = new PaginationValidatorImpl();
        var bcryptEncoder = new BCryptPasswordEncoder(12);
        var auditOrderService = new AuditedOrderServiceImpl(
                orderRepository, certificateRepository, orderValidator, paginationValidator
        );

        userService = new UserServiceImpl(
                userRepository, userValidation, paginationValidator, bcryptEncoder, auditOrderService
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenGetUser_thenCorrectlyReturnsItById() throws ServiceException {
        var given = initUser();

        when(userRepository.findById(given.getId())).thenReturn(Optional.of(given));
        when(
                orderRepository.findAll(Mockito.isA(Specification.class), Mockito.isA(Pageable.class))
        ).thenReturn(new PageImpl<>(new ArrayList<>()));

        var actual = userService.getUserById(given.getId());
        assertEquals(given, actual);
        verify(userRepository).findById(given.getId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenGetUser_thenCorrectlyReturnsItByName() throws ServiceException {
        var given = initUser();

        when(userRepository.getUserByLogin(given.getLogin())).thenReturn(given);
        when(
                orderRepository.findAll(Mockito.isA(Specification.class), Mockito.isA(Pageable.class))
        ).thenReturn(new PageImpl<>(new ArrayList<>()));

        var actual = userService.getUserByLogin(given.getLogin());
        assertEquals(given, actual);
        verify(userRepository).getUserByLogin(given.getLogin());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenAddUser_thenCorrectlyReturnThem() throws ServiceException {
        var given = new ArrayList<User>();
        IntStream.rangeClosed(1, 10).forEach(i -> given.add(initUser(i)));

        var givenSearchCriteria = UserSearchCriteria.getDefaultUserRequestBody();

        when(
                orderRepository.findAll(Mockito.isA(Specification.class), Mockito.isA(Pageable.class))
        ).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(userRepository.findAll(Mockito.isA(Pageable.class))).thenReturn(new PageImpl<>(given));

        var actual = userService.getAllUsersByPage(
                givenSearchCriteria, SIZE, PAGE, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy()
        );
        assertEquals(given, actual);
        verify(userRepository).findAll(Mockito.isA(Pageable.class));
    }
}
