package com.netcracker.model.pet;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.user.Profile;

import java.util.List;
import java.util.Set;

@ObjectType(value = 201)
public class Pet extends BaseEntity {

    //@Attribute(value = ) нет в базе айдишника
    private String petAvatar;
    @Attribute(value = 201)
    private String petName;
    @Attribute(value = 202)
    private Integer petAge;
    @Attribute(value = 300)
    private PetSpecies petSpecies;
    @Attribute(value = 203)
    private String petBreed;
    @Attribute(value = 204)
    private Double petWeight;
    @Attribute(value = 205)
    private Double petHeight;
    @Attribute(value = 206)
    private String petSpecificParam;
    //@Reference(value = ) в профиле нет указания на питомца
    private Profile petOwner;
    @Attribute(value = 301)
    private Status petStatus;
    //TODO SERVICE GETADVERTISEMENTS
    @Reference(value = 15)
    private Set<Advertisement> petAdvertisements;
    //TODO SERVICE GETPHOTOALBUMS
    @Attribute(value = 302)
    private List<PhotoAlbum> petPhotoAlbums;

    public Pet() {
    }

    public Pet(String name) {
        super(name);
    }

    public Pet(String name, String description) {
        super(name, description);
    }

    public String getPetAvatar() {
        return petAvatar;
    }

    public void setPetAvatar(String petAvatar) {
        this.petAvatar = petAvatar;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public PetSpecies getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(PetSpecies petSpecies) {
        this.petSpecies = petSpecies;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public double getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(double petWeight) {
        this.petWeight = petWeight;
    }

    public double getPetHeight() {
        return petHeight;
    }

    public void setPetHeight(double petHeight) {
        this.petHeight = petHeight;
    }

    public String getPetSpecificParam() {
        return petSpecificParam;
    }

    public void setPetSpecificParam(String petSpecificParam) {
        this.petSpecificParam = petSpecificParam;
    }

    public Profile getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(Profile petOwner) {
        this.petOwner = petOwner;
    }

    public Status getPetStatus() {
        return petStatus;
    }

    public void setPetStatus(Status petStatus) {
        this.petStatus = petStatus;
    }

    public Set<Advertisement> getPetAdvertisements() {
        return petAdvertisements;
    }

    public void setAdvertisementPets(Set<Advertisement> advertisements) {
        this.petAdvertisements = advertisements;
    }

    public List<PhotoAlbum> getPetPhotoAlbums() {
        return petPhotoAlbums;
    }

    public void setPetPhotoAlbums(List<PhotoAlbum> petPhotoAlbums) {
        this.petPhotoAlbums = petPhotoAlbums;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petAvatar=" + petAvatar +
                ", petName='" + petName + '\'' +
                ", petAge=" + petAge +
                ", petSpecies=" + petSpecies +
                ", petBreed='" + petBreed + '\'' +
                ", petWeight=" + petWeight +
                ", petHeight=" + petHeight +
                ", petSpecificParam='" + petSpecificParam + '\'' +
                ", petOwner=" + petOwner +
                ", petStatus=" + petStatus +
                ", advertisementPets=" + petAdvertisements +
                ", petPhotoAlbums=" + petPhotoAlbums +
                '}';
    }
}
