package com.netcracker.testdb;

public class ObjectRow {

    private int id;
    private int parent_id;
    private String code;
    private String name;

    ObjectRow(int id, int parent_id, String code, String name) {
        this.id = id;
        this.parent_id = parent_id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID = " + id +
                ",   parent_id = '" + parent_id + '\'' +
                ",   code = '" + code + '\'' +
                ",   name = '" + name + '\'' +
                '}';
    }
}
