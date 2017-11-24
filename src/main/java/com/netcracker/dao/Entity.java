package com.netcracker.dao;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private Integer objectId;
    private Integer objectTypeId;
    private Integer parentId;

    private String name;
    private String description;

    private Map<Pair<Integer, Integer>, Object> attributes;

    private Map<Pair<Integer, Integer>, Integer> references;

    public Entity(){
        attributes = new HashMap<>();
        references = new HashMap<>();
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Pair<Integer, Integer>, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Pair<Integer, Integer>, Object> entity) {
        this.attributes = entity;
    }

    public Map<Pair<Integer, Integer>, Integer> getReferences() {
        return references;
    }

    public void setReferences(Map<Pair<Integer, Integer>, Integer> references) {
        this.references = references;
    }

    @Override
    public java.lang.String toString() {
        return "Entity{" +
                "objectId=" + objectId +
                ", objectTypeId=" + objectTypeId +
                ", parentId=" + parentId +
                ", name=" + name +
                ", description=" + description +
                ", attributes=" + attributes +
                ", references=" + references +
                '}';
    }
}
