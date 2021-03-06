package com.epam.esm.security;

import com.epam.esm.service.AuditedOrderService;
import com.epam.esm.service.exception.ServiceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityUtil {

    private static final Logger log = LogManager.getLogger(UserSecurityUtil.class);

    private final AuditedOrderService auditedOrderService;

    public UserSecurityUtil(AuditedOrderService auditedOrderService) {
        this.auditedOrderService = auditedOrderService;
    }

    public boolean authenticateUserId(Authentication authentication, int userId) {
        try {
            var details = (UserDetailsImpl) authentication.getPrincipal();

            return details != null && details.getId() != 0 && details.getId() == userId;
        } catch (ClassCastException e) {
            log.error("Failed to get user details from authentication");
            return false;
        }
    }

    public boolean authenticateOrderId(Authentication authentication, int orderId) {
        try {
            var details = (UserDetailsImpl) authentication.getPrincipal();
            var order = auditedOrderService.getAuditedOrderById(orderId);
            var user = order.getUser();

            return user != null && user.getId() == details.getId();
        } catch (ClassCastException | ServiceException e) {
            log.error("Failed to get user details from authentication");
            return false;
        }
    }
}
