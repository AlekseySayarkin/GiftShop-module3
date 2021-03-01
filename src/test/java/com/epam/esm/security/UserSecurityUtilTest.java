package com.epam.esm.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
public class UserSecurityUtilTest {

    @InjectMocks
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

        Assertions.assertTrue(validation);
    }

    @Test
    void givenIncorrectToken_throwException() {
        var token = "incorrect token";

        try {
            jwtTokenProvider.validateJwtToken(token);
        } catch (AuthenticationServiceException e) {
            Assertions.assertEquals(e.getMessage(), "Failed to validate jwt token");
        }
    }

    @Test
    void whenCreateToken_CorrectlyRetrieveUsername(){
        var username = "username";
        var role = "USER";
        var token = jwtTokenProvider.createJwtToken(username, role);
        var retrievedUsername = jwtTokenProvider.getUserName(token);

        Assertions.assertEquals(username, retrievedUsername);
    }

    @Test
    void givenIncorrectToken_throwExceptionWhileRetrievingUsername() {
        var token = "incorrect token";

        try {
            jwtTokenProvider.getUserName(token);
        } catch (AuthenticationServiceException e) {
            Assertions.assertEquals(e.getMessage(), "Failed to get username from jwt token");
        }
    }

    @Test
    void givenToken_RetrieveAuthentication() {
        var username = "username";
        var role = "USER";
        var token = jwtTokenProvider.createJwtToken(username, role);

        Mockito.when(userDetailsService.loadUserByUsername(username))
                .thenReturn(new UserDetailsImpl(1, username, "password", null, true));

        var auth = jwtTokenProvider.getAuthentication(token);
        var userDetails = (UserDetailsImpl) auth.getPrincipal();
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(auth.getCredentials(), "");
        Assertions.assertEquals(username, userDetails.getUsername());
    }

    @Test
    void givenInvalidToken_thenThrowExceptionWhileGettingAuthentication() {
        var token = "Invalid token";

        try {
            jwtTokenProvider.getAuthentication(token);
        } catch (AuthenticationServiceException e) {
            Assertions.assertEquals(e.getMessage(), "Failed to get username from jwt token");
        }
    }
}
