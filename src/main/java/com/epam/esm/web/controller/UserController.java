package com.epam.esm.web.controller;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public List<User> getUsers(@RequestParam int limit, @RequestParam int offset)
            throws PersistenceException {
        return userService.getAllUsersByPage(limit, offset);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id) throws PersistenceException {
        return userService.getUser(id);
    }
}
