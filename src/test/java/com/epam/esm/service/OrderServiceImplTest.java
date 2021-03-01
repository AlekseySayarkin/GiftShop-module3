package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.AuditedOrderServiceImpl;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.service.search.criteria.OrderSearchCriteria;
import com.epam.esm.service.util.impl.OrderValidatorImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revisions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class OrderServiceImplTest {

    @InjectMocks
    private AuditedOrderServiceImpl auditedOrderService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository certificateRepository;

    private final static int PAGE = 1;
    private final static int SIZE = 10;

    @BeforeEach
    public void setUp() {
        var orderValidator =  new OrderValidatorImpl();
        var paginationValidator = new PaginationValidatorImpl();
        orderService = new OrderServiceImpl(orderRepository, orderValidator, userRepository);
        auditedOrderService = new AuditedOrderServiceImpl(
                orderRepository, certificateRepository, orderValidator, paginationValidator
        );
    }

    private Order initOrder() {
        var order = new Order();
        order.setId(1);
        order.setTotalCost(10.10);
        order.setActive(true);
        order.setUser(new User(1, "login", "password"));

        var certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setName("certificate");
        certificate.setPrice(10.10);
        order.getGiftCertificateList().add(certificate);

        return order;
    }

    private Order initOrder(int id) {
        var order = new Order();
        order.setId(id);
        order.setTotalCost(10.10);
        order.setActive(true);
        order.setUser(new User(1, "login", "password"));

        return order;
    }

    @Test
    void whenGetOrder_thenCorrectlyReturnsItById() throws ServiceException {
        var givenOrder = initOrder();

        Mockito.when(orderRepository.findById(givenOrder.getId())).thenReturn(java.util.Optional.of(givenOrder));
        Mockito.when(certificateRepository.findRevisions(Mockito.isA(Integer.class))).thenReturn(Revisions.none());

        Order actual = auditedOrderService.getAuditedOrderById(givenOrder.getId());
        Assertions.assertEquals(givenOrder, actual);
        Mockito.verify(orderRepository).findById(givenOrder.getId());
    }

    @Test
    void whenAddOrders_thenCorrectlyReturnThem() throws ServiceException {
        var given = new ArrayList<Order>();
        IntStream.rangeClosed(1, 10).forEach(i -> given.add(initOrder(i)));
        var givenSearchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();

        Mockito.when(
                orderRepository.findAll(Mockito.isA(Pageable.class))
        ).thenReturn(new PageImpl<>(given));
        Mockito.when(certificateRepository.findRevisions(Mockito.isA(Integer.class))).thenReturn(Revisions.none());

        List<Order> actual = auditedOrderService.getAuditedOrdersByPage(givenSearchCriteria, SIZE, PAGE,
                givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(given, actual);
        Mockito.verify(orderRepository).findAll(Mockito.isA(Pageable.class));
    }

    @Test
    void whenAddOrder_thenReturnItId() throws ServiceException {
        var given = initOrder();

        Mockito.when(orderRepository.save(given)).thenReturn(given);
        Mockito.when(userRepository.findById(given.getUser().getId())).thenReturn(Optional.of(given.getUser()));

        var actual = orderService.addUserOrder(given, 1);
        Assertions.assertEquals(actual, given);
        Mockito.verify(orderRepository).save(given);
    }

    @Test
    void whenTryAddEmptyOrder_thenThrowException() {
        Order order = new Order();

        try {
            orderService.addUserOrder(order, 1);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: cost must be positive", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteOrder_thenThrowException() {
        Order given = initOrder();

        try {
            orderService.deleteOrder(given.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to get certificate by it id: " + given.getId(), e.getMessage());
        }
    }
}