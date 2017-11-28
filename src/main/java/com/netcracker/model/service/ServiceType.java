package com.netcracker.model.service;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(value = 7)
public class ServiceType extends BaseEntity {

    @Attribute(value = 25)
    private String serviceType;
    @Reference(value = 24)
    private List<Service> services;

    public ServiceType() {
    }

    public ServiceType(String name) {
        super(name);
    }

    public ServiceType(String name, String description) {
        super(name, description);
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "ServiceType{" +
                "serviceType='" + serviceType + '\'' +
                ", services=" + services +
                '}';
    }
}
