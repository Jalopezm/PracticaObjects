package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.*;
import com.esliceu.PracticaObjects.repos.BucketDAO;
import com.esliceu.PracticaObjects.repos.ObjectDAO;
import com.esliceu.PracticaObjects.repos.UserDAO;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    public List<Objects> allObjects(User user, Bucket bucket) {
        return objectDAO.getAllObjects(user.getNickname(), bucket.getId());
    }


    public List<Bucket> allBuckets(User user) {
        return bucketDAO.getAllBuckets(user.getNickname());
    }

    public void newBucket(String name, String Owner) {
        bucketDAO.newBucket(name, Owner);
    }

    public void updateUser(String name, String nickname, String email, String encritpPass) {
        userDAO.updateUser(name, nickname, email, encritpPass);
    }

    public User getUser(String nickname) {
        return userDAO.getUser(nickname);
    }

    public void newFile(byte[] arrayBytes, int length, String hash) {
        objectDAO.newFile(arrayBytes, length, hash);
    }

    public Bucket getBucket(String uri, String owner) {
        return bucketDAO.getBucket(uri, owner);
    }

    public Objects newObject(int bucketId, String uri, Timestamp from, String owner, Timestamp from1, String contentType) {
        return objectDAO.newObject(bucketId, uri, from, owner, from1, contentType);
    }

    public boolean fileOnDb(String hash) {
        return objectDAO.fileOnDb(hash);
    }

    public Objects getObject(int bucketId, String objectname) {
        return objectDAO.getObject(bucketId, objectname);
    }

    public File getFileFromHash(String hash) {
        return objectDAO.getFileFromHash(hash);
    }

    public void refFileToObject(Objects object, File file) {
        objectDAO.refFileToObject(object, file);
    }

    public List<File> getFileFromObjId(Bucket bucket, Objects o) {
        Objects object = objectDAO.getObject(bucket.getId(), o.getUri());
        return objectDAO.getFileFromObjId(object.getId());
    }

    public int getFileVersion(File createdFile, Objects o) {
        ObjectToFileRef ref = objectDAO.getFileVersion(createdFile, o);
        if (ref == null) {
            return 0;
        }
        return ref.getVersionId();
    }

    public List<ObjectToFileRef> getFileToObject(int id) {
        return objectDAO.getFileToObject(id);
    }

    public File getFileFromFileId(int fid) {
        return objectDAO.getFileFromFileId(fid);
    }

    public Objects getObjectFromObjId(int objid) {
        return objectDAO.getObjectFromObjId(objid);
    }

    public Bucket bucketOnDb(String uri) {
        return bucketDAO.bucketOnDb(uri);
    }

    public void deleteObject(String object, int bucketId) {
        Objects o = objectDAO.getObject(bucketId, object);
        List<File> fileList = objectDAO.getFileFromObjId(o.getId());
        File f = fileList.get(0);
        objectDAO.deleteObject(o, f);
    }

    public void updateLink(String hash) {
        File f = objectDAO.getFileFromHash(hash);
        objectDAO.updateLink(f);
    }

    public void deleteBucket(User user, Bucket bucket) {
        List<Objects> objectsList = objectDAO.getAllObjects(user.getNickname(), bucket.getId());
        for (int i = 0; i < objectsList.size(); i++) {
            List<File> fileList = objectDAO.getFileFromObjId(objectsList.get(i).getId());
            File f = fileList.get(i);
            objectDAO.deleteObject(objectsList.get(i), f);
        }
        objectDAO.deleteBucket(bucket);
    }

}
