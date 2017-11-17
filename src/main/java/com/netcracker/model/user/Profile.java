package com.netcracker.model.user;

import com.netcracker.model.Model;
import com.netcracker.model.Status;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.WallRecord;


import java.util.List;


public class Profile extends Model {

    private String profileAvatar;
    private String profileName;
    private String profileSurname;
    private Integer age;
    private List<String> profileHobbies;
    private List<String> favouriteBreeds;
    private Double currencyBalance;
    private Status profileStatus;
    private User user;
    private List<WallRecord> wallRecords;
    private List<Pet> pets;

    public Profile() {
    }

    public Profile(String name) {
        super(name);
    }

    public Profile(String name, String description) {
        super(name, description);
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileSurname() {
        return profileSurname;
    }

    public void setProfileSurname(String profileSurname) {
        this.profileSurname = profileSurname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getProfileHobbies() {
        return profileHobbies;
    }

    public void setProfileHobbies(List<String> profileHobbies) {
        this.profileHobbies = profileHobbies;
    }

    public List<String> getFavouriteBreeds() {
        return favouriteBreeds;
    }

    public void setFavouriteBreeds(List<String> favouriteBreeds) {
        this.favouriteBreeds = favouriteBreeds;
    }

    public double getCurrencyBalance() {
        return currencyBalance;
    }

    public void setCurrencyBalance(double currencyBalance) {
        this.currencyBalance = currencyBalance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(Status profileStatus) {
        this.profileStatus = profileStatus;
    }

    public List<WallRecord> getWallRecords() {
        return wallRecords;
    }

    public void setWallRecords(List<WallRecord> wallRecords) {
        this.wallRecords = wallRecords;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileAvatar=" + profileAvatar +
                ", profileName='" + profileName + '\'' +
                ", profileSurname='" + profileSurname + '\'' +
                ", age=" + age +
                ", profileHobbies='" + profileHobbies + '\'' +
                ", favouriteBreeds='" + favouriteBreeds + '\'' +
                ", currencyBalance=" + currencyBalance +
                ", user=" + user +
                ", wallRecords=" + wallRecords +
                ", pets=" + pets +
                '}';
    }
}
