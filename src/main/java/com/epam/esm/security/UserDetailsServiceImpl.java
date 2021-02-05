package com.epam.esm.security;

import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return UserDetailsImpl.of(userService.getUserByLogin(username));
        } catch (ServiceException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
