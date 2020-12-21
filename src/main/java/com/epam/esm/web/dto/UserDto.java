package com.epam.esm.web.dto;

import com.epam.esm.model.User;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto extends RepresentationModel<UserDto> {

    private int id;
    private String login;
    private String password;

    public UserDto() {
    }

    public UserDto(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public static List<UserDto> of(List<User> users) {
        return users.stream().map(UserDto::of).collect(Collectors.toList());
    }

    public static UserDto of(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
