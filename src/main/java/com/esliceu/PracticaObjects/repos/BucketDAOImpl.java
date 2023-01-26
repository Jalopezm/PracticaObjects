package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BucketDAOImpl implements BucketDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void newBucket(String name, String Owner) {
        jdbcTemplate.update("Insert into bucket (uri,owner) values (?,?)", name,Owner);
    }
    @Override
    public List<Bucket> getAllBuckets(String nickname) {
        return jdbcTemplate.query("Select * from bucket where owner=?",new BeanPropertyRowMapper<>(Bucket.class), nickname);
    }

    @Override
    public Bucket getBucket(String uri, String owner) {
        List<Bucket> bucketList = jdbcTemplate.query("Select * from bucket where owner=? and uri = ?",new BeanPropertyRowMapper<>(Bucket.class), owner,uri);
        return bucketList.get(0);
    }
}
