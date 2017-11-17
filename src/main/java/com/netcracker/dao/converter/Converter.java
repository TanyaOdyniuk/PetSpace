package com.netcracker.dao.converter;

import com.netcracker.dao.Entity;
import com.netcracker.dao.manager.EntityManager;
import com.netcracker.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Odyniuk on 17/11/2017.
 */
@Component
public class Converter {
    @Autowired
    EntityManager entityManager;

    public Entity convertToEntity(Model model){
        return null;
    }
    public <T extends Model> T convertToModel(Entity entity, Class clazz){
        return null;
    }
}
