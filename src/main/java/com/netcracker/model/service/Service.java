package com.netcracker.model.service;

import com.netcracker.model.Model;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;

import java.util.Set;

public class Service extends Model {
    private String servicePhoto;
    private String serviceName;
    private Double servicePrice;
    private ServiceType serviceType;
    //TODO SERVICE GETUSERS
    private Set<Profile> profiles;

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

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setServicePrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        return "Service{" +
                "servicePhoto=" + servicePhoto +
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice=" + servicePrice +
                ", serviceType=" + serviceType +
                ", profiles=" + profiles +
                '}';
    }
}
