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
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(orderRepository.findById(givenOrder.getId())).thenReturn(java.util.Optional.of(givenOrder));
        when(certificateRepository.findRevisions(Mockito.isA(Integer.class))).thenReturn(Revisions.none());

        Order actual = auditedOrderService.getAuditedOrderById(givenOrder.getId());
        assertEquals(givenOrder, actual);
        verify(orderRepository).findById(givenOrder.getId());
    }

    @Test
    void whenAddOrders_thenCorrectlyReturnThem() throws ServiceException {
        var given = new ArrayList<Order>();
        IntStream.rangeClosed(1, 10).forEach(i -> given.add(initOrder(i)));
        var givenSearchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();

        when(orderRepository.findAll(Mockito.isA(Pageable.class))).thenReturn(new PageImpl<>(given));
        when(certificateRepository.findRevisions(Mockito.isA(Integer.class))).thenReturn(Revisions.none());

        var actual = auditedOrderService.getAuditedOrdersByPage(
                givenSearchCriteria, SIZE, PAGE, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy()
        );
        assertEquals(given, actual);
        verify(orderRepository).findAll(Mockito.isA(Pageable.class));
    }

    @Test
    void whenAddOrder_thenReturnItId() throws ServiceException {
        var given = initOrder();

        when(orderRepository.save(given)).thenReturn(given);
        when(userRepository.findById(given.getUser().getId())).thenReturn(Optional.of(given.getUser()));

        var actual = orderService.addUserOrder(given, 1);
        assertEquals(actual, given);
        verify(orderRepository).save(given);
    }

    @Test
    void whenTryAddEmptyOrder_thenThrowException() {
        var order = new Order();

        try {
            orderService.addUserOrder(order, 1);
        } catch (ServiceException e) {
            assertEquals("Failed to validate: cost must be positive", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteOrder_thenThrowException() {
        var given = initOrder();

        try {
            orderService.deleteOrder(given.getId());
        } catch (ServiceException e) {
            assertEquals("Failed to get certificate by it id: " + given.getId(), e.getMessage());
        }
    }
}