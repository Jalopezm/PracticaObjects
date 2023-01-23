package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.Objects;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.repos.BucketDAO;
import com.esliceu.PracticaObjects.repos.ObjectDAO;
import com.esliceu.PracticaObjects.repos.UserDAO;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    ObjectDAO objectDAO;
    @Autowired
    EncriptPass encriptPass;
    @Autowired
    BucketDAO bucketDAO;

    public void newUser(String name, String password) {
        User u = new User(name, password);
        userDAO.addUser(u);
    }


    public boolean logUser(String name, String password) {
        return userDAO.logUser(name, password);
    }

    public boolean validateUser(String name) {
        return userDAO.validateUser(name);
    }

    public void deleteUser(String name, String password) {
        userDAO.deleteUser(name, password);
    }

    public int getUserID(String name) {
        return userDAO.getUserID(name);
    }
    public List<Objects> allObjects(User user) {
        return objectDAO.getAllObjects(getUserID(user.getName()));
    }


    public List<Bucket> allBuckets(User user) {
        return bucketDAO.getAllBuckets(getUserID(user.getName()));
    }

    public void newBucket(String name, int idOwner) {
        bucketDAO.newBucket(name,idOwner);
    }
}
