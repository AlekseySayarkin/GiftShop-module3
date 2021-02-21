package com.epam.esm.web.api;

import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.AuthRequestDto;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

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
}
