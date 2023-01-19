package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void addUser(User u) {
        jdbcTemplate.update("Insert into user (name,password) values (?,?)", u.getName(),u.getPassword());
    }
}
