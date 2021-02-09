package com.epam.esm.web.controller;

import com.epam.esm.model.User;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.AuthRequestDto;
import com.epam.esm.web.dto.JwtTokenResponseObject;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private final ModelAssembler<UserDto> modelAssembler;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          JwtTokenProvider jwtTokenProvider, ModelAssembler<UserDto> modelAssembler) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelAssembler = modelAssembler;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
    }

    @PostMapping("/auth/signup")
    public EntityModel<JwtTokenResponseObject> signup(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        User user = new User();
        user.setLogin(requestDto.getLogin());
        user.setPassword(requestDto.getPassword());
        user = userService.addUser(user);

        return EntityModel.of(getResponse(requestDto, user));
    }

    @PostMapping("/auth/login")
    public EntityModel<JwtTokenResponseObject> login(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.getLogin(), requestDto.getPassword())
        );
        User user = userService.getUserByLogin(requestDto.getLogin());

        return EntityModel.of(getResponse(requestDto, user));
    }

    @PostMapping("/auth/logout")
    public HttpStatus logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);

        return HttpStatus.OK;
    }

    @GetMapping("/user/info")
    public EntityModel<JwtTokenResponseObject> oAuth2Login(@AuthenticationPrincipal OAuth2User user)
            throws ServiceException {
        User returnedUser = userService.getOrAddByLogin(user);

        String token = jwtTokenProvider.createJwtToken(
                returnedUser.getLogin(), returnedUser.getRole().getRoleType().toString()
        );

        return EntityModel.of(new JwtTokenResponseObject(token, modelAssembler.toModel(UserDto.of(returnedUser))));
    }

    private JwtTokenResponseObject getResponse(AuthRequestDto requestDto, User user) {
        String token = jwtTokenProvider.createJwtToken(
                requestDto.getLogin(), user.getRole().getRoleType().toString()
        );

        return new JwtTokenResponseObject(token, modelAssembler.toModel(UserDto.of(user)));
    }
}
