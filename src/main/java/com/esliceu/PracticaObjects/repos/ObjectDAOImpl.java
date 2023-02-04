package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.File;
import com.esliceu.PracticaObjects.model.ObjectToFileRef;
import com.esliceu.PracticaObjects.model.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ObjectDAOImpl implements ObjectDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Objects> getAllObjects(String nickname, int bucketId) {
        return jdbcTemplate.query("Select * from object where owner= ? and bucketId=?", new BeanPropertyRowMapper<>(Objects.class), nickname, bucketId);
    }

    @Override
    public void newFile(byte[] arrayBytes, int length, String hash) {
        jdbcTemplate.update("Insert into file (body,contentLength,hash) values (?,?,?)", arrayBytes, length, hash);
    }


    @Override
    public Objects newObject(int bucketId, String uri, Timestamp from, String owner, Timestamp from1, String contentType) {
        jdbcTemplate.update("Insert into object (bucketId,uri,lastModified,owner,created,contentType) values (?,?,?,?,?,?)", bucketId, uri, from, owner, from1, contentType);
        List<Objects> objectsList = jdbcTemplate.query("Select * from object where bucketId=?", new BeanPropertyRowMapper<>(Objects.class), bucketId);
        return objectsList.get(objectsList.size() - 1);
    }

    @Override
    public boolean fileOnDb(String hash) {
        List<File> fileList = jdbcTemplate.query("Select * from file where hash = ?", new BeanPropertyRowMapper<>(File.class), hash);
        return fileList.size() > 0;
    }

    @Override
    public Objects getObject(int bucketId, String objectname) {
        List<Objects> objectsList = jdbcTemplate.query("Select * from object where bucketId = ? and uri = ?", new BeanPropertyRowMapper<>(Objects.class), bucketId, objectname);
        if (objectsList.size() > 0) {
            return objectsList.get(0);
        }
        return null;
    }

    @Override
    public File getFileFromHash(String hash) {
        List<File> fileList = jdbcTemplate.query("Select * from file where hash = ?", new BeanPropertyRowMapper<>(File.class), hash);
        return fileList.get(0);
    }

    @Override
    public void refFileToObject(Objects object, File file) {
        jdbcTemplate.update("Insert into filetoobject (idObject,idFile,date,versionId) values (?,?,?,?)", object.getId(), file.getId(), object.getCreated(), file.getVersion());

    }

    @Override
    public List<File> getFileFromObjId(int id) {
        List<ObjectToFileRef> list = jdbcTemplate.query("Select * from filetoobject where idObject = ?", new BeanPropertyRowMapper<>(ObjectToFileRef.class), id);
        List<File> fileList = new ArrayList<>();
        for (ObjectToFileRef objectToFileRef : list) {
            List<File> f = jdbcTemplate.query("Select * from file where id =?", new BeanPropertyRowMapper<>(File.class), objectToFileRef.getIdFile());
            fileList.add(f.get(0));
        }
        return fileList;
    }

    @Override
    public ObjectToFileRef getFileVersion(File createdFile, Objects object) {
        List<ObjectToFileRef> refList = jdbcTemplate.query("Select versionId from filetoobject where idFile = ? AND idObject = ? ORDER BY versionId DESC;", new BeanPropertyRowMapper<>(ObjectToFileRef.class), createdFile.getId(), object.getId());
        if (refList.size() > 0) {
            return refList.get(0);
        }
        return null;
    }

    @Override
    public List<ObjectToFileRef> getFileToObject(int id) {
        return jdbcTemplate.query("Select * from filetoobject where idObject = ?", new BeanPropertyRowMapper<>(ObjectToFileRef.class), id);
    }

    @Override
    public File getFileFromFileId(int fid) {
        List<File> fileList = jdbcTemplate.query("Select * from file where id = ?", new BeanPropertyRowMapper<>(File.class), fid);
        return fileList.get(0);
    }

    @Override
    public Objects getObjectFromObjId(int objid) {
        List<Objects> objectsList = jdbcTemplate.query("Select * from object where id = ?", new BeanPropertyRowMapper<>(Objects.class), objid);
        return objectsList.get(0);
    }

    @Override
    public void deleteObject(Objects o, File f) {
        if (f.getLink() <= 1) {
            jdbcTemplate.update("Delete from file where hash=?", f.getHash());
        } else {
            jdbcTemplate.update("Update file SET link=? where hash=?", (f.getLink() - 1), f.getHash());
        }
        jdbcTemplate.update("Delete from filetoobject where idObject=? and idFile=?", o.getId(), f.getId());
        jdbcTemplate.update("Delete from object where uri=?", o.getUri());
    }

    @Override
    public void updateLink(File f) {
        int link = f.getLink();
        link += 1;
        jdbcTemplate.update("UPDATE file SET link = ? WHERE hash=?", link, f.getHash());
    }

    @Override
    public void deleteBucket(Bucket bucket) {
        jdbcTemplate.update("Delete from bucket where id = ?", bucket.getId());
    }


}
