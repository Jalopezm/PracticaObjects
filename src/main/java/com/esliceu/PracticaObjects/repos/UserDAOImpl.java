package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void addUser(User u) {
        jdbcTemplate.update("Insert into user (name,password) values (?,?)", u.getName(), u.getPassword());
    }

    @Override
    public boolean logUser(String name, String password) {
        List<User> userList = jdbcTemplate.query("Select * from user where name = ? and password = ?", new BeanPropertyRowMapper<>(User.class), name, password);
        for (int i = 0; i < userList.size(); i++) {
            User currUser = userList.get(i);
            if (currUser.getName().equals(name) && currUser.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateUser(String name) {
        List<User> userList = jdbcTemplate.query("Select name from user where name = ? ", new BeanPropertyRowMapper<>(User.class), name);
        for (int i = 0; i < userList.size(); i++) {
            User currUser = userList.get(i);
            if (currUser.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteUser(String name, String password) {
    jdbcTemplate.update("Delete from user where name = ? and password = ?", name, password);
    }
}
