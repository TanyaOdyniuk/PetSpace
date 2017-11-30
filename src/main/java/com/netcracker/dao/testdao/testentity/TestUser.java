package com.netcracker.dao.testdao.testentity;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ObjectType(1)
public class TestUser extends BaseEntity {
    @Attribute(4)
    private String username;
    @Attribute(5)
    private String surname;
    @Attribute(6)
    private List<String> favouritePets;
    @Reference(7)
    private List<TestPet> myPets;
    @Reference(8)
    private Set<TestCity> mycities;

    public TestUser() {
        favouritePets = new ArrayList<>();
        myPets = new ArrayList<>();
        mycities = new HashSet<>();
    }

    public TestUser(String name) {
        super(name);
        favouritePets = new ArrayList<>();
        myPets = new ArrayList<>();
        mycities = new HashSet<>();
    }

    public TestUser(String name, String description) {
        super(name, description);
        favouritePets = new ArrayList<>();
        myPets = new ArrayList<>();
        mycities = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<String> getFavouritePets() {
        return favouritePets;
    }

    public void setFavouritePets(List<String> favouritePets) {
        this.favouritePets = favouritePets;
    }

    public List<TestPet> getMyPets() {
        return myPets;
    }

    public void setMyPets(List<TestPet> myPets) {
        this.myPets = myPets;
    }

    public Set<TestCity> getMycities() {
        return mycities;
    }

    public void setMycities(Set<TestCity> mycities) {
        this.mycities = mycities;
    }
}
