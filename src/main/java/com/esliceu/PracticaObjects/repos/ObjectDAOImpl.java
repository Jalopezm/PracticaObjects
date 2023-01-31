package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.File;
import com.esliceu.PracticaObjects.model.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class ObjectDAOImpl implements ObjectDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Objects> getAllObjects(String nickname,int bucketId) {
        return jdbcTemplate.query("Select * from object where owner= ? and bucketId=?",new BeanPropertyRowMapper<>(Objects.class), nickname,bucketId);
    }

    @Override
    public void newFile(byte[] arrayBytes, int length, String hash) {
        jdbcTemplate.update("Insert into file (body,contentLength,hash) values (?,?,?)" ,arrayBytes,length,hash);
    }


    @Override
    public Objects newObject(int bucketId, String uri, Timestamp from, String owner, Timestamp from1, String contentType) {
        jdbcTemplate.update("Insert into object (bucketId,uri,lastModified,owner,created,contentType) values (?,?,?,?,?,?)" ,bucketId,uri,from,owner,from1,contentType);
        List<Objects> objectsList = jdbcTemplate.query("Select * from object where bucketId=?",new BeanPropertyRowMapper<>(Objects.class),bucketId);
        Objects object = objectsList.get(objectsList.size()-1);
        return object;
    }

    @Override
    public boolean fileOnDb(String hash) {
        List<File> fileList = jdbcTemplate.query("Select * from file where hash = ?" ,new BeanPropertyRowMapper<>(File.class), hash);
        if (fileList.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public Objects getObject(int bucketId, String objectname) {
        List<Objects> objectsList = jdbcTemplate.query("Select * from object where bucketId = ? and uri = ?",new BeanPropertyRowMapper<>(Objects.class),bucketId,objectname);
        Objects object = objectsList.get(0);
        return object;
    }

    @Override
    public File getFile(int id) {
        List<File> fileList = jdbcTemplate.query("Select * from file where id = ?",new BeanPropertyRowMapper<>(File.class),id);
        File file = fileList.get(0);
        return  file;
    }

    @Override
    public void refFileToObject(Objects object, File file) {
        jdbcTemplate.update("Insert into filetoobject (idObject,idFile,date,versionId) values (?,?,?,?)" ,object.getId(),file.getId(),object.getCreated(),file.getVersion());

    }


}
