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
        jdbcTemplate.update("Insert into user (name,nickname,email,password) values (?,?,?,?)", u.getName(),u.getNickname(),u.getEmail(), u.getPassword());
    }

    @Override
    public boolean logUser(String nickname, String password) {
        List<User> userList = jdbcTemplate.query("Select * from user where nickname = ? and password = ?", new BeanPropertyRowMapper<>(User.class), nickname, password);
        for (int i = 0; i < userList.size(); i++) {
            User currUser = userList.get(i);
            if (currUser.getNickname().equals(nickname) && currUser.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateUser(String name) {
        List<User> userList = jdbcTemplate.query("Select nickname from user where nickname = ? ", new BeanPropertyRowMapper<>(User.class), name);
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

    @Override
    public int getUserID(String nickname) {
        List<User> idList = jdbcTemplate.query("Select id from user where nickname = ?", new BeanPropertyRowMapper<>(User.class), nickname);
        return idList.get(0).getId();
    }

    @Override
    public void updateUser(String name, String nickname, String email, String encritpPass) {
        jdbcTemplate.update("UPDATE user SET name = ?, email = ?, password = ? where nickname = ",name,email,encritpPass,nickname);

    }

    @Override
    public User getUser(String nickname) {
        List<User> userList = jdbcTemplate.query("Select * from user where nickname = ?", new BeanPropertyRowMapper<>(User.class), nickname);
        return userList.get(0);
    }
}
