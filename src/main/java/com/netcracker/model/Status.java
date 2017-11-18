package com.netcracker.model;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.group.Group;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;

import java.util.List;

public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String status;
    private List<Group> groups;
    private List<Profile> profiles;
    private List<Advertisement> advertisements;
    private List<Pet> pets;

    Status(String status) {
        this.status = status;
    }

    public String getType() {
        return status;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
