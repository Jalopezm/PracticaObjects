package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.File;
import com.esliceu.PracticaObjects.model.Objects;

import java.sql.Timestamp;
import java.util.List;

public interface ObjectDAO {
    List<Objects> getAllObjects(String nickname,int bucketId);

    void newFile(byte[] arrayBytes, int length, String hash);

    Objects newObject(int bucketId, String uri, Timestamp from, String owner, Timestamp from1, String contentType);

    boolean fileOnDb(String hash);

    Objects getObject(int bucketId, String objectname);

    File getFileFromHash(String hash);

    void refFileToObject(Objects object, File file);

    File getFileFromObjId(int id);
}
