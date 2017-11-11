package com.netcracker.model.service;

public enum ServiceType {
    SERVICETYPEONE("Service type one"),
    SERVICETYPETWO("Service type two");

    private String type;

    ServiceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
