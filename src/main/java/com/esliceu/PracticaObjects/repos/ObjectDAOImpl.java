package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ObjectDAOImpl implements ObjectDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Objects> getAllObjects(int id) {
        return jdbcTemplate.query("Select * from object where userID= ?",new BeanPropertyRowMapper<>(Objects.class), id);
    }

}
