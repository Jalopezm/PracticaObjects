package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.repos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public void newUser(String name, String nick, String email, String password) {
        User u = new User(name, nick, email, password);
        userDAO.addUser(u);
    }

    public boolean logUser(String nickname, String password) {
        return userDAO.logUser(nickname, password);
    }

    public boolean validateUser(String name) {
        return userDAO.validateUser(name);
    }

    public void deleteUser(String name, String password) {
        userDAO.deleteUser(name, password);
    }

    public void updateUser(String name, String nickname, String email, String encritpPass) {
        userDAO.updateUser(name, nickname, email, encritpPass);
    }

    public User getUser(String nickname) {
        return userDAO.getUser(nickname);
    }
}