package com.netcracker.model.service;

import com.netcracker.model.Model;
import com.netcracker.model.user.User;

import java.util.Set;

public class Service extends Model {
    private Object servicePhoto;
    private String serviceName;
    private Double servicePrice;
    private ServiceType serviceType;
    private Set<User> users;

    public Service() {
    }

    public Service(String name) {
        super(name);
    }

    public Service(String name, String description) {
        super(name, description);
    }

    public Object getServicePhoto() {
        return servicePhoto;
    }

    public void setServicePhoto(Object servicePhoto) {
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
