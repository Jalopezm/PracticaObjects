package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.repos.UserDAO;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

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


    public void logUser(String name, String password) {
        String encriptPassword = encriptPass.encritpPass(password);
    }
}
