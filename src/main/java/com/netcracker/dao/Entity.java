package com.netcracker.dao;

import com.netcracker.model.BaseEntity;
import javafx.util.Pair;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Entity extends BaseEntity {

    private Map<Pair<BigInteger, Integer>, Object> attributes;

    private Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> references;

    public Entity(){
        this(null, null);
    }

    public Entity(String name) {
        this(name, null);
    }

    public Entity(String name, String description) {
        super(name, description);
        attributes = new HashMap<>();
        references = new HashMap<>();
    }

    public Map<Pair<BigInteger, Integer>, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Pair<BigInteger, Integer>, Object> entity) {
        this.attributes = entity;
    }

    public Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> getReferences() {
        return references;
    }

    public void setReferences(Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> references) {
        this.references = references;
    }

    @Override
    public java.lang.String toString() {
        return "Entity{" +
                "objectId=" + this.getObjectId() +
                ", objectTypeId=" + objectTypeId +
                ", parentId=" + parentId +
                ", name=" + name +
                ", description=" + description +
                ", attributes=" + attributes +
                ", references=" + references +
                '}';
    }
}
