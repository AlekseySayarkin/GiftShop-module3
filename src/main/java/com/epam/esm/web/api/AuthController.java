package com.epam.esm.web.api;

import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.AuthRequestDto;
import com.epam.esm.web.dto.JwtTokenResponseObject;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    private final UserService userService;
    private final ModelAssembler<UserDto> modelAssembler;

    public AuthController(UserService userService, ModelAssembler<UserDto> modelAssembler) {
        this.userService = userService;
        this.modelAssembler = modelAssembler;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
    }

    @PostMapping("/auth/signup")
    public HttpStatus signup(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        var user = new User();
        user.setLogin(requestDto.getLogin());
        user.setPassword(requestDto.getPassword());
        userService.addUser(user);

        return HttpStatus.CREATED;
    }

    @PostMapping("/auth/login")
    public EntityModel<JwtTokenResponseObject> login(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        User user = userService.getUserByLogin(requestDto.getLogin());

        return EntityModel.of(getResponse(requestDto, user));
    }

    @PostMapping("/auth/logout")
    public HttpStatus logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);

        return HttpStatus.OK;
    }
    private JwtTokenResponseObject getResponse(AuthRequestDto requestDto, User user) {
        String token = jwtTokenProvider.createJwtToken(
                requestDto.getLogin(), user.getRole().getRoleType().toString()
        );

        return new JwtTokenResponseObject(token, modelAssembler.toModel(UserDto.of(user)));
    }
}
