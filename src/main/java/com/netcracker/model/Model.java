package com.netcracker.model;

import java.math.BigInteger;

public abstract class Model {

    private String name;
    private String description;
    private BigInteger objectId;
    private BigInteger parentId;
    private BigInteger objectTypeId;

    public Model() {
    }

    public Model(String name) {
        this.name = name;
    }

    public Model(String name, String description) {
        this.name = name;
        this.description = description;
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

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public BigInteger getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(BigInteger objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        return objectId.equals(model.objectId);
    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    @Override
    public String toString() {
        return "Model{" +
                "objectId=" + objectId +
                '}';
    }
}
