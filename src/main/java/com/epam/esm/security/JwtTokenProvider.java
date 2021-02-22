package com.epam.esm.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {

    String createJwtToken(String username, String role);
    boolean validateJwtToken(String token);
    Authentication getAuthentication(String token);
    String getUserName(String token);
    String resolveToken(HttpServletRequest request);
}
