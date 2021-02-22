package com.epam.esm.security;

import com.epam.esm.service.AuditedOrderService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    private final AuditedOrderService auditedOrderService;

    public UserSecurity(AuditedOrderService auditedOrderService) {
        this.auditedOrderService = auditedOrderService;
    }

    public boolean hasUserId(Authentication authentication, int userId) {
        try {
            var details = (UserDetailsImpl) authentication.getPrincipal();

            return details != null && details.getId() != 0 && details.getId() == userId;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean orderHasUserId(Authentication authentication, int orderId) {
        try {
            var details = (UserDetailsImpl) authentication.getPrincipal();
            var order = auditedOrderService.getAuditedOrderById(orderId);

            return order.getUser().getId() == details.getId();
        } catch (ClassCastException | ServiceException e) {
            return false;
        }
    }
}
