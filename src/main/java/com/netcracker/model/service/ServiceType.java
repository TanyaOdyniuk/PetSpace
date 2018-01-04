package com.netcracker.model.service;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(ServiceConstant.SERVT_TYPE)
public class ServiceType extends BaseEntity {

    @Attribute(ServiceConstant.SERVT_TNAME)
    private String serviceType;

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

    @Override
    public String toString() {
        return "ServiceType{" +
                "serviceType='" + serviceType + '\'' +
                '}';
    }
}
