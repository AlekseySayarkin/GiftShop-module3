package com.epam.esm.web.api;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class AuthController {

    private final UserService userService;
    private final ModelAssembler<UserDto> modelAssembler;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, ModelAssembler<UserDto> modelAssembler,
                          JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.modelAssembler = modelAssembler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
    }

    @PostMapping("/auth/signup")
    public EntityModel<JwtTokenResponseObject> signup(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        var user = new User();
        user.setLogin(requestDto.getLogin());
        user.setPassword(requestDto.getPassword());
        userService.addUser(user);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));

        return EntityModel.of(getResponse(user));
    }

    @PostMapping("/auth/login")
    public EntityModel<JwtTokenResponseObject> login(@RequestBody AuthRequestDto requestDto) throws ServiceException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getLogin(), requestDto.getPassword())
        );
        var user = userService.getUserByLogin(requestDto.getLogin());

        return EntityModel.of(getResponse(user));
    }

    private JwtTokenResponseObject getResponse(User user) {
        return new JwtTokenResponseObject(
                jwtTokenProvider.createJwtToken(user.getLogin(), user.getRole().getRoleType().toString())
        );
    }
}
