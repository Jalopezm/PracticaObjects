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
    public List<Objects> getAllObjects(String nickname) {
        return jdbcTemplate.query("Select * from object where owner= ?",new BeanPropertyRowMapper<>(Objects.class), nickname);
    }

    @Override
    public void newFile(byte[] arrayBytes, int length, String hash) {
        jdbcTemplate.update("Insert into file (body,contentLength,hash) values (?,?,?)" ,arrayBytes,length,hash);
    }


    @Override
    public void newObject(int bucketId, String uri, Timestamp from, String owner, Timestamp from1, String contentType) {
        jdbcTemplate.update("Insert into object (bucketId,uri,lastModified,owner,created,contentType) values (?,?,?,?,?,?)" ,bucketId,uri,from,owner,from1,contentType);
    }

    @Override
    public boolean fileOnDb(String hash) {
        List<File> fileList = jdbcTemplate.query("Select * from file where hash = ?" ,new BeanPropertyRowMapper<>(File.class), hash);
        if (fileList.size() > 0){
            return true;
        }
        return false;
    }


}
