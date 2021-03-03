package com.epam.esm.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class JwtTokenProviderImplTest {

    private JwtTokenProviderImpl jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProviderImpl(userDetailsService);
    }

    @Test
    void whenCreateToken_thenCorrectlyValidateIt(){
        var username = "username";
        var role = "USER";
        var token = jwtTokenProvider.createJwtToken(username, role);
        var validation = jwtTokenProvider.validateJwtToken(token);

        assertTrue(validation);
    }

    @Test
    void givenIncorrectToken_throwException() {
        var token = "incorrect token";

        try {
            jwtTokenProvider.validateJwtToken(token);
        } catch (AuthenticationServiceException e) {
            assertEquals(e.getMessage(), "Failed to validate jwt token");
        }
    }

    @Test
    void whenCreateToken_CorrectlyRetrieveUsername(){
        var username = "username";
        var role = "USER";
        var token = jwtTokenProvider.createJwtToken(username, role);
        var retrievedUsername = jwtTokenProvider.getUserName(token);

        assertEquals(username, retrievedUsername);
    }

    @Test
    void givenIncorrectToken_throwExceptionWhileRetrievingUsername() {
        var token = "incorrect token";

        try {
            jwtTokenProvider.getUserName(token);
        } catch (AuthenticationServiceException e) {
            assertEquals(e.getMessage(), "Failed to get username from jwt token");
        }
    }

    @Test
    void givenToken_RetrieveAuthentication() {
        var username = "username";
        var role = "USER";
        var token = jwtTokenProvider.createJwtToken(username, role);

        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(new UserDetailsImpl(1, username, "password", null, true));

        var auth = jwtTokenProvider.getAuthentication(token);
        var userDetails = (UserDetailsImpl) auth.getPrincipal();
        assertNotNull(auth);
        assertEquals(auth.getCredentials(), "");
        assertEquals(username, userDetails.getUsername());
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    void givenInvalidToken_thenThrowExceptionWhileGettingAuthentication() {
        var token = "Invalid token";

        try {
            jwtTokenProvider.getAuthentication(token);
        } catch (AuthenticationServiceException e) {
            assertEquals(e.getMessage(), "Failed to get username from jwt token");
        }
    }
}
