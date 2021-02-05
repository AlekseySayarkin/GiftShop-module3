package com.epam.esm.security;

import com.epam.esm.web.dto.OrderDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserSecurity {

    public boolean hasUserId(Authentication authentication, int userId) {
        try {
            UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();

            return details != null && details.getId() != 0 && details.getId() == userId;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean hasUserId(Authentication authentication, EntityModel<OrderDto> order) {
        try {
            UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();

            return Objects.requireNonNull(order.getContent()).getUserId() == details.getId();
        } catch (ClassCastException e) {
            return false;
        }
    }
}
