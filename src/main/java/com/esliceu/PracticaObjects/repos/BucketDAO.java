package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Bucket;

import java.util.List;

public interface BucketDAO {
    void newBucket(String name, int idOwner);

    List<Bucket> getAllBuckets(int userID);
}
