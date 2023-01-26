package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Objects;

import java.sql.Timestamp;
import java.util.List;

public interface ObjectDAO {
    List<Objects> getAllObjects(String id);

    void newFile(byte[] arrayBytes, int length, String hash);

    void newObject(int bucketId, String uri, Timestamp from, String owner, Timestamp from1, String contentType);

    boolean fileOnDb(String hash);
}
