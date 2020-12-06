package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MySQLUserDao")
public class SQLUserDaoImpl implements UserDao {

    private static final String SQL_GET_TAG_BY_NAME = "select * from Users where Login = ?";
    private static final String SQL_GET_TAG_BY_ID = "select * from Users where Id = ?";
    private static final String SQL_GET_TAGS_BY_PAGE = "select * from Users limit ? offset ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    @Autowired
    public SQLUserDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = rowMapper;
    }

    @Override
    public User getUser(String login) {
        return jdbcTemplate.queryForObject(SQL_GET_TAG_BY_NAME, userRowMapper, login);
    }

    @Override
    public User getUser(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_TAG_BY_ID, userRowMapper, id);
    }

    @Override
    public List<User> getAllUsersByPage(int limit, int offset) {
        return jdbcTemplate.query(SQL_GET_TAGS_BY_PAGE, userRowMapper, limit, offset);
    }
}
