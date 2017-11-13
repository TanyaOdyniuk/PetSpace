package com.netcracker.model.service;

import java.util.List;

public enum ServiceType {
    SERVICE_TYPE_ONE("Service type one"),
    SERVICE_TYPE_TWO("Service type two");

    private String type;
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
