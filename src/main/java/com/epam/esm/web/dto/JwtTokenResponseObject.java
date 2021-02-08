package com.epam.esm.web.dto;

import org.springframework.hateoas.EntityModel;

public class JwtTokenResponseObject {

    private final String jwtToken;
    private final EntityModel<UserDto> userDto;

    public JwtTokenResponseObject(String jwtToken, EntityModel<UserDto> userDto) {
        this.jwtToken = jwtToken;
        this.userDto = userDto;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public EntityModel<UserDto> getUserDto() {
        return userDto;
    }
}
