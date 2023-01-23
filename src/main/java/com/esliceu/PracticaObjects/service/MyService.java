package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.repos.UserDAO;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    EncriptPass encriptPass;

    public void newUser(String name, String password) {
        String encriptPassword = encriptPass.encritpPass(password);
        User u = new User(name, encriptPassword);
        userDAO.addUser(u);
    }


    public boolean logUser(String name, String password) {
        String encriptPassword = encriptPass.encritpPass(password);
        return userDAO.logUser(name,encriptPassword);
    }

    public boolean validateUser(String name) {
        return userDAO.validateUser(name);
    }

    public void deleteUser(String name, String password) {
        String encriptPassword = encriptPass.encritpPass(password);
        userDAO.deleteUser(name,encriptPassword);
    }
}
