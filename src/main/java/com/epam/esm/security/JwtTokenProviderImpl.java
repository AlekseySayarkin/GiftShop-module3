package com.epam.esm.security;

import com.epam.esm.model.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private static final Logger log = LogManager.getLogger(JwtTokenProviderImpl.class);

    private final UserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "authorities";
    private static final String ROLE_KEY = "role";

    private String secretKey = "secret";

    private static final int EXPIRATION_IN_MILLISECONDS = 3600000;

    public JwtTokenProviderImpl(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createJwtToken(String username, String role) {
        var claims = Jwts.claims().setSubject(username);
        claims.put(ROLE_KEY, role);
        claims.put(AUTHORITIES_KEY, getAuthoritiesFromRole(role));
        var now = new Date();
        var expiration = new Date(now.getTime() + EXPIRATION_IN_MILLISECONDS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String[] getAuthoritiesFromRole(String role) {
        var authorities = new ArrayList<String>();
        if (role.equalsIgnoreCase(Role.RoleType.USER.toString())) {
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Role.RoleType.USER.getAuthorities();
            simpleGrantedAuthorities.forEach(a -> authorities.add(a.getAuthority()));
        } else if (role.equalsIgnoreCase(Role.RoleType.ADMIN.toString())) {
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Role.RoleType.ADMIN.getAuthorities();
            simpleGrantedAuthorities.forEach(a -> authorities.add(a.getAuthority()));
        }

        return authorities.toArray(new String[0]);
    }

    @Override
    public boolean validateJwtToken(String token) throws AuthenticationServiceException {
        try {
            var claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return new Date().before(claimsJws.getBody().getExpiration());
        } catch (ExpiredJwtException e) {
            log.error("Failed to validate jwt token: jwt token expired");
            throw new AuthenticationServiceException("Failed to validate jwt token: jwt token expired");
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to validate jwt token");
            throw new AuthenticationServiceException("Failed to validate jwt token");
        }
    }

    @Override
    public Authentication getAuthentication(String token) throws AuthenticationServiceException {
        var details = userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(details, "", getAuthoritiesFromToken(token));
    }

    @SuppressWarnings("unchecked")
    private List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) throws AuthenticationServiceException {
        try {
            var stringAuthorities = (List<String>) Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get(AUTHORITIES_KEY);

            var authorities = new ArrayList<SimpleGrantedAuthority>();
            stringAuthorities.forEach(a -> authorities.add(new SimpleGrantedAuthority(a)));

            return authorities;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to get authorities from jwt token");
            throw new AuthenticationServiceException("Failed to get authorities from jwt token");
        }
    }

    @Override
    public String getUserName(String token) throws AuthenticationServiceException {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to get username from jwt token");
            throw new AuthenticationServiceException("Failed to get username from jwt token");
        }
    }

    @Override
    public String resolveToken(HttpServletRequest request) throws AuthenticationServiceException {
        try {
            var token = request.getHeader(AUTHORIZATION_HEADER);
            return token == null || token.isEmpty() ? null : token.substring(6);
        } catch (IndexOutOfBoundsException e) {
            log.error("Jwt token should start with 'Basic'");
            throw new AuthenticationServiceException("Jwt token should start with 'Basic'");
        }
    }
}
