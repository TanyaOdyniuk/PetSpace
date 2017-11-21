package com.netcracker.dao.managerapi;

import com.netcracker.dao.Entity;
import com.netcracker.dao.converter.Converter;
import com.netcracker.dao.manager.EntityManager;
import com.netcracker.model.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ManagerAPI {

    @Autowired
    private EntityManager manager;

    @Autowired
    private Converter converter;


    @Transactional
    public Entity create(BaseEntity baseEntity) {
        Entity entity = manager.create(converter.convertToEntity(baseEntity));
        baseEntity.setObjectId(entity.getObjectId());
        return entity;
    }

    public void update(BaseEntity baseEntity) {
        if (baseEntity.getObjectId() == null) {
            create(baseEntity);
        }
        manager.update(converter.convertToEntity(baseEntity));
    }

    public void delete(Integer objectId, int forceDelete) {
        manager.delete(objectId, forceDelete);
    }

    public <T extends BaseEntity> T getById(Integer objectId, Class baseEntityClass) {
        Entity entity = manager.getById(objectId);
        return (T)converter.convertToBaseEntity(entity, baseEntityClass);

    }

    public List<BaseEntity> getAll(Integer objectTypeId, Class baseEntityClass) {
        List<Entity> entities = manager.getAll(objectTypeId);
        List<BaseEntity> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            BaseEntity baseEntity = converter.convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    public List<BaseEntity> getObjectsBySQL(String sqlQuery, Class baseEntityClass) {
        List<Entity> entities = manager.getBySQL(sqlQuery);
        List<BaseEntity> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            BaseEntity baseEntity = converter.convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }
}