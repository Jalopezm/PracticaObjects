package com.esliceu.PracticaObjects.service;

import com.esliceu.PracticaObjects.model.*;
import com.esliceu.PracticaObjects.repos.ObjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ObjectService {
    @Autowired
    ObjectDAO objectDAO;

    public List<Objects> allObjects(User user, Bucket bucket) {
        return objectDAO.getAllObjects(user.getNickname(), bucket.getId());
    }

    public void newFile(byte[] arrayBytes, int length, String hash) {
        objectDAO.newFile(arrayBytes, length, hash);
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


}
