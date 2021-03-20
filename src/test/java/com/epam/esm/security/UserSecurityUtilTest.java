package com.epam.esm.security;

import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.AuditedOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserSecurityUtilTest {

    private UserSecurityUtil userSecurityUtil;

    @Mock
    private AuditedOrderService auditedOrderService;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        var orders = new HashSet<Order>();
        var order = new Order();
        order.setId(2);
        orders.add(order);
        var details = UserDetailsImpl.of(new User(
                1, "login", "password", orders, Role.getUserRole())
        );

        var authorities = new ArrayList<SimpleGrantedAuthority>();
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Role.RoleType.USER.getAuthorities();
        simpleGrantedAuthorities.forEach(a -> authorities.add(new SimpleGrantedAuthority(a.getAuthority())));
        authentication = new UsernamePasswordAuthenticationToken(details, "", authorities);

        userSecurityUtil = new UserSecurityUtil(auditedOrderService);
    }

    @Test
    void givenUserAuthentication_ReturnTrue() {
        var userId = 1;
        var auth = userSecurityUtil.authenticateUserId(authentication, userId);
        assertTrue(auth);
    }

    @Test
    void givenUserAuthentication_ReturnFalse() {
        var userId = 2;
        var auth = userSecurityUtil.authenticateUserId(authentication, userId);
        assertFalse(auth);
    }

    @Test
    void givenOrderAuthentication_ReturnTrue() {
        var orderId = 1;
        var order = new Order();
        order.setId(1);
        var user = new User();
        user.setId(1);
        order.setUser(user);

        when(auditedOrderService.getAuditedOrderById(orderId)).thenReturn(order);
        var auth = userSecurityUtil.authenticateOrderId(authentication, orderId);

        assertTrue(auth);
        verify(auditedOrderService).getAuditedOrderById(orderId);
    }

    @Test
    void givenOrderAuthentication_ReturnFalse() {
        var orderId = 2;
        var order = new Order();
        order.setId(1);
        var user = new User();
        user.setId(1);
        order.setUser(user);

        Mockito.when(auditedOrderService.getAuditedOrderById(orderId)).thenReturn(order);
        var auth = userSecurityUtil.authenticateOrderId(authentication, orderId);

        assertTrue(auth);
        verify(auditedOrderService).getAuditedOrderById(orderId);
    }
}
