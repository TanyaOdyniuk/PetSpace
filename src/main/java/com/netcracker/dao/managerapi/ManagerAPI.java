package com.netcracker.dao.managerapi;

import com.netcracker.dao.Entity;
import com.netcracker.dao.converter.Converter;
import com.netcracker.dao.manager.EntityManager;
import com.netcracker.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Odyniuk on 17/11/2017.
 */

@Repository
public class ManagerAPI {

    @Autowired
    private EntityManager manager;

    @Autowired
    private Converter converter;


    @Transactional
    public Entity create(Model model) {
        Entity entity;
        entity = manager.create(converter.convertToEntity(model));
        model.setObjectId(entity.getObjectId());
        return entity;
    }

    public void update(Model model) {
        if (model.getObjectId() == null) {
            create(model);
        }
        manager.update(converter.convertToEntity(model));
    }

    public void delete(Integer objectId) {
        manager.delete(objectId);
    }

    public <T extends Model> T getById(Integer objectId, Class modelClass) {
        Entity entity = manager.getById(objectId);
        T model = null;
        model = converter.convertToModel(entity, modelClass);
        return model;
    }

    public List getAll(Integer objectTypeId, Class modelClass) {
        List<Entity> entities = manager.getAll(objectTypeId);
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            Model model = null;
            model = converter.convertToModel(entities.get(i), modelClass);
            models.add(model);
        }
        return models;
    }
}