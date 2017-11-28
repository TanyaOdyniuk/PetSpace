package com.netcracker.model;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.group.Group;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(value = 3)
public class Status extends BaseEntity {

    @Attribute(value = 3)
    private String status;
    @Reference(value = 427)
    private List<Group> groups;
    @Reference(value = 419)
    private List<Profile> profiles;
    @Reference(value = 13)
    private List<Advertisement> advertisements;
    @Reference(value = 301)
    private List<Pet> pets;

    public Status() {
    }

    public Status(String name) {
        super(name);
    }

    public Status(String name, String description) {
        super(name, description);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                ", groups=" + groups +
                ", profiles=" + profiles +
                ", advertisements=" + advertisements +
                ", pets=" + pets +
                '}';
    }
}
