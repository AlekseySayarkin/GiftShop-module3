package com.epam.esm.dao.mapper;

import com.epam.esm.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    private static final String USER_ID_COLUMN = "ID";
    private static final String USER_LOGIN_COLUMN = "Login";
    private static final String USER_PASSWORD_COLUMN = "Password";

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(USER_ID_COLUMN));
        user.setLogin(rs.getString(USER_LOGIN_COLUMN));
        user.setPassword(rs.getString(USER_PASSWORD_COLUMN));

        return user;
    }
}
