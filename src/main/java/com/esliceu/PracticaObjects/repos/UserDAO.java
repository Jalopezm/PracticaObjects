package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.User;

public interface UserDAO {
    void addUser(User u);

    boolean logUser(String name, String encriptPassword);

    boolean validateUser(String name);

    void deleteUser(String name, String password);
}
