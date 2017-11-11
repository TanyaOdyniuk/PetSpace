package com.netcracker.model.pet;

import com.netcracker.model.Model;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;

import java.util.List;

public class Pet extends Model {

    private PhotoRecord avatarRecord;
    private String petName;
    private int age;
    private PetSpecies species;
    private String breed;
    private double weight;
    private double height;
    private String specificParam;
    private Profile profile;
    private Status petStatus;
    private List<Advertisement> advertisements;
    private List<PhotoAlbum> photoAlbums;

    public Pet() {
    }

    public Pet(String name) {
        super(name);
    }

    public Pet(String name, String description) {
        super(name, description);
    }

    public PhotoRecord getAvatarRecord() {
        return avatarRecord;
    }

    public void setAvatarRecord(PhotoRecord avatarRecord) {
        this.avatarRecord = avatarRecord;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PetSpecies getSpecies() {
        return species;
    }

    public void setSpecies(PetSpecies species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getSpecificParam() {
        return specificParam;
    }

    public void setSpecificParam(String specificParam) {
        this.specificParam = specificParam;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Status getPetStatus() {
        return petStatus;
    }

    public void setPetStatus(Status petStatus) {
        this.petStatus = petStatus;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisementPets(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public List<PhotoAlbum> getPhotoAlbums() {
        return photoAlbums;
    }

    public void setPhotoAlbums(List<PhotoAlbum> photoAlbums) {
        this.photoAlbums = photoAlbums;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "avatarRecord=" + avatarRecord +
                ", petName='" + petName + '\'' +
                ", age=" + age +
                ", species=" + species +
                ", breed='" + breed + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", specificParam='" + specificParam + '\'' +
                ", profile=" + profile +
                ", petStatus=" + petStatus +
                ", advertisementPets=" + advertisements +
                ", photoAlbums=" + photoAlbums +
                '}';
    }
}
