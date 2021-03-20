package com.epam.esm.web.dto;

public class JwtTokenResponseObject {

    private final String jwtToken;

    public JwtTokenResponseObject(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
