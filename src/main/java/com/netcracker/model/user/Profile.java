package com.netcracker.model.user;

import com.netcracker.model.Model;
import com.netcracker.model.album.AvatarAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.AvatarRecord;
import com.netcracker.model.record.WallRecord;

import java.util.List;


public class Profile extends Model{

    private AvatarRecord profileAvatar;
    private String profileName;
    private String profileSurname;
    private int age;
    private String profileHobbies;
    private String favouriteBreeds;
    private double currencyBalance;
    private User user;
    private AvatarAlbum avatarAlbum;
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

    public AvatarRecord getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(AvatarRecord profileAvatar) {
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

    public String getProfileHobbies() {
        return profileHobbies;
    }

    public void setProfileHobbies(String profileHobbies) {
        this.profileHobbies = profileHobbies;
    }

    public String getFavouriteBreeds() {
        return favouriteBreeds;
    }

    public void setFavouriteBreeds(String favouriteBreeds) {
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

    public AvatarAlbum getAvatarAlbum() {
        return avatarAlbum;
    }

    public void setAvatarAlbum(AvatarAlbum avatarAlbum) {
        this.avatarAlbum = avatarAlbum;
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
                ", avatarAlbum=" + avatarAlbum +
                ", wallRecords=" + wallRecords +
                ", pets=" + pets +
                '}';
    }
}
