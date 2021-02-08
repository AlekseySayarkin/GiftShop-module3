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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ModelAssembler<UserDto> modelAssembler;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder,
                          ModelAssembler<UserDto> modelAssembler) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.modelAssembler = modelAssembler;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
    }

    @PostMapping("/signup")
    public EntityModel<JwtTokenResponseObject> signup(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        User user = new User();
        user.setLogin(requestDto.getLogin());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user = userService.addUser(user);

        return EntityModel.of(getResponse(requestDto, user));
    }

    @PostMapping("/login")
    public EntityModel<JwtTokenResponseObject> login(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.getLogin(), requestDto.getPassword())
        );
        User user = userService.getUserByLogin(requestDto.getLogin());

        return EntityModel.of(getResponse(requestDto, user));
    }

    @PostMapping("/logout")
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
