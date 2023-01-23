package com.esliceu.PracticaObjects.repos;

import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.Objects;

import java.util.List;

public interface ObjectDAO {
    List<Objects> getAllObjects(int id);

}
