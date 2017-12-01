package com.netcracker.model.service;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;

import java.util.Set;

@ObjectType(ServiceConstant.SERV_TYPE)
public class Service extends BaseEntity {
    @Attribute(ServiceConstant.SERV_PHOTO)
    private String servicePhoto;
    @Attribute(ServiceConstant.SERV_NAME)
    private String serviceName;
    @Attribute(ServiceConstant.SERV_PRICE)
    private Double servicePrice;
    @Reference(ServiceConstant.SERV_STYPE)
    private ServiceType serviceType;
    //TODO SERVICE GETUSERS
    @Reference(UsersProfileConstant.PROFILE_SERVICES)
    private Set<User> users;

    public Service() {
    }

    public Service(String name) {
        super(name);
    }

    public Service(String name, String description) {
        super(name, description);
    }

    public String getServicePhoto() {
        return servicePhoto;
    }

    public void setServicePhoto(String servicePhoto) {
        this.servicePhoto = servicePhoto;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Service{" +
                "servicePhoto=" + servicePhoto +
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice=" + servicePrice +
                ", serviceType=" + serviceType +
                ", users=" + users +
                '}';
    }
}
