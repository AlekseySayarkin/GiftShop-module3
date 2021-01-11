package com.epam.esm.web.dto;

import com.epam.esm.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDto extends RepresentationModel<UserDto> {

    private int id;
    private String login;
    private String password;
    private Set<EntityModel<OrderDto>> orders;

    public UserDto() {
    }

    public static List<UserDto> of(List<User> users) {
        return users.stream().map(UserDto::of).collect(Collectors.toList());
    }

    public static UserDto of(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        Set<EntityModel<OrderDto>> orderDto = new HashSet<>();
        user.getOrders().forEach(o ->
                orderDto.add(EntityModel.of(OrderDto.of(o))));
        userDto.setOrders(orderDto);

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

    public Set<EntityModel<OrderDto>> getOrders() {
        return orders;
    }

    public void setOrders(Set<EntityModel<OrderDto>> orders) {
        this.orders = orders;
    }
}
