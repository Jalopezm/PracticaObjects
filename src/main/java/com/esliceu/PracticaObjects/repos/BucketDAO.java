package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Bucket;

import java.util.List;

public interface BucketDAO {
    void newBucket(String name, String Owner);
    List<Bucket> getAllBuckets(String user);
    Bucket getBucket(String uri, String owner);
    Bucket bucketOnDb(String uri);
}
