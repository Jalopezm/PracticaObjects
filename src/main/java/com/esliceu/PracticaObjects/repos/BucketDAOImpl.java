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
    public void newBucket(String name, int idOwner) {
        jdbcTemplate.update("Insert into bucket (uri,idOwner) values (?,?)", name,idOwner);
    }
    @Override
    public List<Bucket> getAllBuckets(int id) {
        return jdbcTemplate.query("Select * from bucket where idOwner=?",new BeanPropertyRowMapper<>(Bucket.class), id);
    }
}
