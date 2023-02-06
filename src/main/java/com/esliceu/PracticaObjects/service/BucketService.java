package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.File;
import com.esliceu.PracticaObjects.model.Objects;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.repos.BucketDAO;
import com.esliceu.PracticaObjects.repos.ObjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BucketService {
    @Autowired
    ObjectService objectService;
    @Autowired
    BucketDAO bucketDAO;
    @Autowired
    ObjectDAO objectDAO;

    public List<Bucket> allBuckets(User user) {
        return bucketDAO.getAllBuckets(user.getNickname());
    }

    public void newBucket(String name, String Owner) {
        bucketDAO.newBucket(name, Owner);
    }

    public Bucket getBucket(String uri, String owner) {
        return bucketDAO.getBucket(uri, owner);
    }

    public Bucket bucketOnDb(String uri) {
        return bucketDAO.bucketOnDb(uri);
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