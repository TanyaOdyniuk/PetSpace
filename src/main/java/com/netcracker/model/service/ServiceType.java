package com.netcracker.model.service;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;

import java.util.List;

@ObjectType(value = 7)
public enum ServiceType {
    SERVICE_TYPE_ONE("Service type one"),
    SERVICE_TYPE_TWO("Service type two");

    @Attribute(value = 25)
    private String type;
    @Reference(value = 24)
    private List<Service> services;

    ServiceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
